package com.distributedstorage.backend.model;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;
    private Long fileSize;
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // REQUIRED by JPA
    public FileMetadata() {
    }

    public FileMetadata(String fileName, String filePath, Long fileSize, LocalDateTime uploadedAt){
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.uploadedAt = uploadedAt;
    }

    public Long getId(){
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public User getUser() {
        return user;
    }

    // ADD THIS METHOD
    public void setUser(User user) {
        this.user = user;
    }
   @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Chunk> chunks;
}