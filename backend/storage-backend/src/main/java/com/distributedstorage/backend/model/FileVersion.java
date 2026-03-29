package com.distributedstorage.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.Index;
@Entity
@Table(
    name = "file_version",
    indexes = {
        @Index(name = "idx_version_file", columnList = "file_id"),
        @Index(name = "idx_version_file_version", columnList = "file_id,versionNumber")
    }
)
public class FileVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer versionNumber;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileMetadata file;

    public FileVersion(){}

    public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public Integer getVersionNumber() {
    return versionNumber;
}

public void setVersionNumber(Integer versionNumber) {
    this.versionNumber = versionNumber;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public FileMetadata getFile() {
    return file;
}

public void setFile(FileMetadata file) {
    this.file = file;
}

}