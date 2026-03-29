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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final FileVersionService fileVersionService;

    @Value("${storage.base-path}")
    private String basePath;

    // ✔ Fixed constructor
    public FileController(FileService fileService, FileVersionService fileVersionService) {
        this.fileService = fileService;
        this.fileVersionService = fileVersionService;
    }

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

    @DeleteMapping("/{id}")
    public ApiResponseDTO<String> deleteFile(@PathVariable Long id){

        fileService.deleteFile(id);

        return new ApiResponseDTO<>(
                "SUCCESS",
                "File deleted successfully",
                null
        );
    }

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

            FileMetadata file = fileService.getFileById(id); // ✔ get filename
            byte[] fileData = fileService.downloadFile(id);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + file.getFileName()) // ✔ fixed
                    .body(fileData);

        } catch (Exception e) {
            throw new RuntimeException("File download failed");
        }
    }
    // Updates an existing file — replaces old chunks with new file and creates new version
        @PutMapping("/{id}/update")
        public ApiResponseDTO<FileUploadResponseDTO> updateFile(
                @PathVariable Long id,
                @RequestParam("file") MultipartFile file
        ) {

        try {
                // Call service to handle update workflow
                FileMetadata updatedFile = fileService.updateFile(id, file);

                // Build response DTO with updated file details
                FileUploadResponseDTO response = new FileUploadResponseDTO(
                        updatedFile.getId(),
                        updatedFile.getFileName(),
                        updatedFile.getFileSize()
                );

                return new ApiResponseDTO<>(
                        "SUCCESS",
                        "File updated successfully",
                        response
                );

        } catch (Exception e) {
                throw new RuntimeException("File update failed: " + e.getMessage());
        }
    }
}