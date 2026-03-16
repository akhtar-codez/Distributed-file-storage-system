package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import com.distributedstorage.backend.dto.FileUploadResponseDTO;
import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.model.FileVersion;
import com.distributedstorage.backend.service.FileService;
import com.distributedstorage.backend.service.FileVersionService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final FileVersionService fileVersionService;

    @Value("${storage.base-path}")
    private String basePath;

    // Single constructor for dependency injection
    public FileController(FileService fileService, FileVersionService fileVersionService) {
        this.fileService = fileService;
        this.fileVersionService = fileVersionService;
    }

    // Upload file
    @PostMapping("/upload")
    public ApiResponseDTO<FileUploadResponseDTO> uploadFile(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam Long userId
    ) {

        try {

            FileMetadata savedFile = fileService.processFileUpload(file, userId);

            FileUploadResponseDTO response =
                    new FileUploadResponseDTO(
                            savedFile.getId(),
                            savedFile.getFileName(),
                            savedFile.getFileSize()
                    );

            return new ApiResponseDTO<>(
                    "SUCCESS",
                    "File uploaded successfully",
                    response
            );

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    // Get all files
    @GetMapping
    public ApiResponseDTO<List<FileUploadResponseDTO>> getAllFiles() {

        List<FileMetadata> files = fileService.getAllFiles();

        List<FileUploadResponseDTO> response =
                files.stream()
                        .map(file -> new FileUploadResponseDTO(
                                file.getId(),
                                file.getFileName(),
                                file.getFileSize()
                        ))
                        .toList();

        return new ApiResponseDTO<>(
                "SUCCESS",
                "Files fetched successfully",
                response
        );
    }

    // Get file by ID
    @GetMapping("/{id}")
    public ApiResponseDTO<FileUploadResponseDTO> getFileById(@PathVariable Long id){

        FileMetadata file = fileService.getFileById(id);

        FileUploadResponseDTO response =
                new FileUploadResponseDTO(
                        file.getId(),
                        file.getFileName(),
                        file.getFileSize()
                );

        return new ApiResponseDTO<>(
                "SUCCESS",
                "File fetched successfully",
                response
        );
    }

    // Delete file
    @DeleteMapping("/{id}")
    public ApiResponseDTO<String> deleteFile(@PathVariable Long id){

        fileService.deleteFile(id);

        return new ApiResponseDTO<>(
                "SUCCESS",
                "File deleted successfully",
                null
        );
    }

    // Get version history
    @GetMapping("/{id}/versions")
    public ApiResponseDTO<List<Integer>> getFileVersions(@PathVariable Long id){

        FileMetadata file = fileService.getFileById(id);

        List<FileVersion> versions = fileVersionService.getVersionsByFile(file);

        List<Integer> versionNumbers =
                versions.stream()
                        .map(FileVersion::getVersionNumber)
                        .toList();

        return new ApiResponseDTO<>(
                "SUCCESS",
                "File versions fetched successfully",
                versionNumbers
        );
    }

    @GetMapping("/{id}/download")
public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {

    try {

        byte[] fileData = fileService.downloadFile(id);

        return ResponseEntity.ok()
                .header("Content-Disposition","attachment; filename=file")
                .body(fileData);

    } catch (Exception e) {
        throw new RuntimeException("File download failed");
    }
}
}