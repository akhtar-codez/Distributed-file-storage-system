package com.distributedstorage.backend.repository;

import com.distributedstorage.backend.model.Chunk;
import com.distributedstorage.backend.model.FileMetadata;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChunkRepository extends JpaRepository<Chunk, Long> {

    List<Chunk> findByFile(FileMetadata file);
List<Chunk> findByFileOrderByChunkIndexAsc(FileMetadata file);
}