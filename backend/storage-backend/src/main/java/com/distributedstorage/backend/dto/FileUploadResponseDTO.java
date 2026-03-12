package com.distributedstorage.backend.dto;

public class FileUploadResponseDTO {
    
    private Long fileId;
    private String fileName;
    private Long fileSize;

    public FileUploadResponseDTO(Long fileId, String fileName, Long fileSize) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
    
    public Long getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }
}
