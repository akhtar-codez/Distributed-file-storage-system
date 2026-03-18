package com.distributedstorage.backend.model;
import jakarta.persistence.Index;
import jakarta.persistence.*;

@Entity
@Table(
    name = "chunk",
    indexes = {
        @Index(name = "idx_chunk_file", columnList = "file_id"),
        @Index(name = "idx_chunk_file_order", columnList = "file_id,chunkIndex")
    }
)
public class Chunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer chunkIndex;

    private String chunkPath;

    private Long chunkSize;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileMetadata file;

    public Chunk(){}

    public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public Integer getChunkIndex() {
    return chunkIndex;
}

public void setChunkIndex(Integer chunkIndex) {
    this.chunkIndex = chunkIndex;
}

public String getChunkPath() {
    return chunkPath;
}

public void setChunkPath(String chunkPath) {
    this.chunkPath = chunkPath;
}

public Long getChunkSize() {
    return chunkSize;
}

public void setChunkSize(Long chunkSize) {
    this.chunkSize = chunkSize;
}

public FileMetadata getFile() {
    return file;
}

public void setFile(FileMetadata file) {
    this.file = file;
}

}