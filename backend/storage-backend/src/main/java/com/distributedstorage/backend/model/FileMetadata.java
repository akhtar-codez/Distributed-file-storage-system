package com.distributedstorage.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private Long fileSize;
    private LocalDateTime uploadedAt;

    // Many files can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // One file has many chunks — delete chunks when file is deleted
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chunk> chunks;

    // One file has many versions — delete versions when file is deleted
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileVersion> versions;

    // Required by JPA — no-arg constructor
    public FileMetadata() {}

    // Constructor for creating new file metadata
    public FileMetadata(String fileName, String filePath, Long fileSize, LocalDateTime uploadedAt) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public Long getFileSize() { return fileSize; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public User getUser() { return user; }
    public List<Chunk> getChunks() { return chunks; }
    public List<FileVersion> getVersions() { return versions; }

    // Setters
    // Links file to its owner
    public void setUser(User user) { this.user = user; }

    // Updates file name — used during file update operation
    public void setFileName(String fileName) { this.fileName = fileName; }

    // Updates file size — used during file update operation
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    // Updates upload timestamp — used during file update operation
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}