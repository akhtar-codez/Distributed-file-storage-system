package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import com.distributedstorage.backend.dto.FileUploadResponseDTO;
import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.service.FileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
public class FileController {
    
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ApiResponseDTO<FileUploadResponseDTO> uploadFile(
            @RequestParam String fileName,
            @RequestParam Long fileSize
    ) {
        FileMetadata savedFile = fileService.saveMetadata(
                fileName,
                "storage/node1/" + fileName,
                fileSize
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
}
