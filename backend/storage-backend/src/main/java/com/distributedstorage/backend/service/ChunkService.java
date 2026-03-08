package com.distributedstorage.backend.service;

import com.distributedstorage.backend.model.Chunk;
import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.repository.ChunkRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChunkService {

    private final ChunkRepository chunkRepository;

    public ChunkService(ChunkRepository chunkRepository) {
        this.chunkRepository = chunkRepository;
    }

    public Chunk saveChunk(Chunk chunk){
        return chunkRepository.save(chunk);
    }
    public List<Chunk> getChunksByFile(FileMetadata file){
    return chunkRepository.findByFile(file);
}
}