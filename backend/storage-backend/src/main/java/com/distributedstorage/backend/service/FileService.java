package com.distributedstorage.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import com.distributedstorage.backend.dto.FileUploadResponseDTO;
import com.distributedstorage.backend.exception.ResourceNotFoundException;
import com.distributedstorage.backend.model.Chunk;
import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.repository.FMDRepository;
import com.distributedstorage.backend.repository.UserRepository;
import com.distributedstorage.backend.storage.NodeManager;

@Service
public class FileService {

    // Service for chunk operations
    private final ChunkService chunkService;

    // Repository for file metadata database operations
    private final FMDRepository fmdRepository;

    // Repository for user database operations
    private final UserRepository userRepository;

    // Service for file versioning operations
    private final FileVersionService fileVersionService;

    // Constructor injection — Spring provides all dependencies automatically
    public FileService(
            FMDRepository fmdRepository,
            UserRepository userRepository,
            FileVersionService fileVersionService,
            ChunkService chunkService
    ) {
        this.fmdRepository = fmdRepository;
        this.userRepository = userRepository;
        this.fileVersionService = fileVersionService;
        this.chunkService = chunkService;
    }

    // Saves file metadata to database and creates initial version entry
    public FileMetadata saveMetadata(String fileName, String filePath, Long fileSize, Long userId) {

        // Fetch user who uploaded the file
        User user = userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Create metadata object with file details
        FileMetadata fileMetadata = new FileMetadata(
                fileName,
                filePath,
                fileSize,
                LocalDateTime.now()
        );

        // Link file to the uploading user
        fileMetadata.setUser(user);

        // Persist metadata to database
        FileMetadata savedFile = fmdRepository.save(fileMetadata);

        // Create version 1 entry for this file
        fileVersionService.createNewVersion(savedFile);

        return savedFile;
    }

    // Handles complete file upload workflow — metadata + chunking
    @Transactional
    public FileMetadata processFileUpload(MultipartFile file, Long userId) throws Exception {

        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();
        String filePath = "storage/node1/" + fileName;

        // Save metadata first to get the fileId
        FileMetadata savedFile = saveMetadata(fileName, filePath, fileSize, userId);

        // Split file into chunks and store on disk
        try (InputStream inputStream = file.getInputStream()) {
            createChunks(inputStream, savedFile);
        }

        return savedFile;
    }

    // Handles file update — replaces old chunks with new file and creates new version
    @Transactional
    public FileMetadata updateFile(Long fileId, MultipartFile newFile) throws Exception {

        // Fetch existing file metadata — throws 404 if not found
        FileMetadata existingFile = getFileById(fileId);

        // Step 1 — Delete old chunk files from disk to free up space
        deleteChunksFromDisk(existingFile);

        // Step 2 — Delete old chunk records from database
        List<Chunk> oldChunks = chunkService.getChunksOrdered(existingFile);
        for (Chunk chunk : oldChunks) {
            chunkService.deleteChunk(chunk);
        }

        // Step 3 — Update file metadata with new file details
        existingFile.setFileName(newFile.getOriginalFilename());
        existingFile.setFileSize(newFile.getSize());
        existingFile.setUploadedAt(LocalDateTime.now());

        // Step 4 — Save updated metadata to database
        FileMetadata updatedFile = fmdRepository.save(existingFile);

        // Step 5 — Store new chunks on disk and in database
        try (InputStream inputStream = newFile.getInputStream()) {
            createChunks(inputStream, updatedFile);
        }

        // Step 6 — Create new version entry to track this update
        fileVersionService.createNewVersion(updatedFile);

        return updatedFile;
    }
    // Returns all files stored in the system
    public List<FileMetadata> getAllFiles() {
        return fmdRepository.findAll();
    }

    // Fetches file metadata by ID — throws exception if not found
    public FileMetadata getFileById(Long id) {
        return fmdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
    }

    // Deletes file metadata from database
    public void deleteFile(Long id) {
        FileMetadata file = getFileById(id);
        fmdRepository.delete(file);
    }

    // Splits file into 8KB chunks and distributes across storage nodes
    public void createChunks(InputStream inputStream, FileMetadata fileMetadata) throws Exception {

        int chunkSize = 1024 * 8; // 8KB per chunk
        byte[] buffer = new byte[chunkSize];

        // Get available storage nodes for distribution
        List<String> nodes = NodeManager.getAvailableNodes();

        int bytesRead;
        int chunkIndex = 0;

        while ((bytesRead = inputStream.read(buffer)) != -1) {

            // Distribute chunks across nodes in round-robin fashion
            String node = nodes.get(chunkIndex % nodes.size());
            String chunkPath = "storage/" + node + "/" + fileMetadata.getFileName() + "_chunk_" + chunkIndex;

            // Write chunk bytes to disk
            try (FileOutputStream fos = new FileOutputStream(chunkPath)) {
                fos.write(buffer, 0, bytesRead);
            }

            // Save chunk metadata in database
            chunkService.createChunkMetadata(
                    fileMetadata,
                    chunkIndex,
                    chunkPath,
                    (long) bytesRead
            );

            chunkIndex++;
        }
    }

    // Reassembles file from chunks and returns as byte array for download
    public byte[] downloadFile(Long fileId) throws Exception {

        // Fetch file metadata
        FileMetadata file = getFileById(fileId);

        // Get chunks in correct order for reassembly
        List<Chunk> chunks = chunkService.getChunksOrdered(file);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Read each chunk from disk and append to output stream
        for (Chunk chunk : chunks) {
            try (FileInputStream fis = new FileInputStream(chunk.getChunkPath())) {
                byte[] buffer = fis.readAllBytes();
                outputStream.write(buffer);
            }
        }

        return outputStream.toByteArray();
    }

    // Deletes all physical chunk files from disk for a given file
    private void deleteChunksFromDisk(FileMetadata file) {

        // Get all chunks belonging to this file in order
        List<Chunk> chunks = chunkService.getChunksOrdered(file);

        // Delete each chunk file from disk
        for (Chunk chunk : chunks) {

            // Create File object pointing to chunk location on disk
            java.io.File chunkFile = new java.io.File(chunk.getChunkPath());

            // Only delete if file actually exists on disk
            if (chunkFile.exists()) {
                chunkFile.delete();
            }
        }
    }

    // Returns all files belonging to a specific user as DTOs
    public List<FileUploadResponseDTO> getFilesByUser(Long userId) {

        // Validate user exists before fetching files
        userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Map FileMetadata entities to DTOs to avoid circular reference
        return fmdRepository.findByUserId(userId)
                .stream()
                .map(f -> new FileUploadResponseDTO(f.getId(), f.getFileName(), f.getFileSize()))
                .toList();
    }
}