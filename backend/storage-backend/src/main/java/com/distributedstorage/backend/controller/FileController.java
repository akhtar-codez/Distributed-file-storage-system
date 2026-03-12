package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import com.distributedstorage.backend.dto.FileUploadResponseDTO;
import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @Value("${storage.base-path}")
private String basePath;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Upload file metadata
    @PostMapping("/upload")
    public ApiResponseDTO<FileUploadResponseDTO> uploadFile(
              @RequestParam String fileName,
        @RequestParam Long fileSize,
        @RequestParam Long userId
    ) {

      FileMetadata savedFile = fileService.saveMetadata(
        fileName,
        basePath + "/node1/" + fileName,
        fileSize,
        userId
);

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

    // DELETE file metadata
    @DeleteMapping("/{id}")
    public ApiResponseDTO<String> deleteFile(@PathVariable Long id){

        fileService.deleteFile(id);

        return new ApiResponseDTO<>(
                "SUCCESS",
                "File deleted successfully",
                null
        );
    }
}