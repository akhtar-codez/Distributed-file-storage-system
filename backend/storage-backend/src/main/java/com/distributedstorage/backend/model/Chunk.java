package com.distributedstorage.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chunk")
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

}