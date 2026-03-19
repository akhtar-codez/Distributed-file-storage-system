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
import com.distributedstorage.backend.model.Chunk;
import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.repository.FMDRepository;
import com.distributedstorage.backend.repository.UserRepository;
import com.distributedstorage.backend.storage.NodeManager;

@Service
public class FileService {

    private final ChunkService chunkService;
    private final FMDRepository fmdRepository;
    private final UserRepository userRepository;
    private final FileVersionService fileVersionService;

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

    // Save metadata for uploaded file
    public FileMetadata saveMetadata(String fileName, String filePath, Long fileSize, Long userId) {

        // Fetch user who uploaded the file
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create metadata object
        FileMetadata fileMetadata = new FileMetadata(
                fileName,
                filePath,
                fileSize,
                LocalDateTime.now()
        );

        // Attach file to user
        fileMetadata.setUser(user);

        // Save metadata
        FileMetadata savedFile = fmdRepository.save(fileMetadata);

        // Create version entry
        fileVersionService.createNewVersion(savedFile);

        return savedFile;
    }

    // Handle file upload workflow
@Transactional
public FileMetadata processFileUpload(MultipartFile file, Long userId) throws Exception {

    String fileName = file.getOriginalFilename();
    Long fileSize = file.getSize();
    String filePath = "storage/node1/" + fileName;

    // Save metadata
    FileMetadata savedFile = saveMetadata(fileName, filePath, fileSize, userId);

    // Split file into chunks
    try (InputStream inputStream = file.getInputStream()) {
        createChunks(inputStream, savedFile);
    }

    return savedFile;
}
    // Fetch all stored files
    public List<FileMetadata> getAllFiles() {
        return fmdRepository.findAll();
    }

    // Fetch file metadata by ID
    public FileMetadata getFileById(Long id) {
        return fmdRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    // Delete file metadata
    public void deleteFile(Long id) {
        FileMetadata file = getFileById(id);
        fmdRepository.delete(file);
    }

    // Split file into chunks and store metadata
    public void createChunks(InputStream inputStream, FileMetadata fileMetadata) throws Exception {

        int chunkSize = 1024 * 1024; // 1MB
        byte[] buffer = new byte[chunkSize];

        int bytesRead;
        int chunkIndex = 0;

        while ((bytesRead = inputStream.read(buffer)) != -1) {

        List<String> nodes = NodeManager.getAvailableNodes();
        String node = nodes.get(chunkIndex % nodes.size());

String chunkPath = "storage/" + node + "/" + fileMetadata.getFileName() + "_chunk_" + chunkIndex;

            // Write chunk to disk safely
            try (FileOutputStream fos = new FileOutputStream(chunkPath)) {
                fos.write(buffer, 0, bytesRead);
            }

            // Save chunk metadata
            chunkService.createChunkMetadata(
                    fileMetadata,
                    chunkIndex,
                    chunkPath,
                    (long) bytesRead
            );

            chunkIndex++;
        }
    }
    public byte[] downloadFile(Long fileId) throws Exception {

    FileMetadata file = getFileById(fileId);

    List<Chunk> chunks = chunkService.getChunksOrdered(file);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    for(Chunk chunk : chunks){

       try (FileInputStream fis = new FileInputStream(chunk.getChunkPath())) {
    byte[] buffer = fis.readAllBytes();
    outputStream.write(buffer);
}
    }

    return outputStream.toByteArray();
}

}