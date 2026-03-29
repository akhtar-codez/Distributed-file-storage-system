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

    // Save chunk directly (existing functionality)
    public Chunk saveChunk(Chunk chunk){
        return chunkRepository.save(chunk);
    }

    // Fetch all chunks belonging to a specific file
    public List<Chunk> getChunksByFile(FileMetadata file){
        return chunkRepository.findByFile(file);
    }

    // Create and store chunk metadata automatically
    public Chunk createChunkMetadata(FileMetadata file, int chunkIndex, String chunkPath, long chunkSize){

        // Create new chunk object
        Chunk chunk = new Chunk();

        // Set the order of the chunk in the file
        chunk.setChunkIndex(chunkIndex);

        // Path where this chunk is stored on disk
        chunk.setChunkPath(chunkPath);

        // Size of the chunk in bytes
        chunk.setChunkSize(chunkSize);

        // Link this chunk to the parent file metadata
        chunk.setFile(file);

        // Save chunk metadata in database
        return chunkRepository.save(chunk);
    }
    public List<Chunk> getChunksOrdered(FileMetadata file){
    return chunkRepository.findByFileOrderByChunkIndexAsc(file);
    }
    // Deletes a single chunk record from the database
    public void deleteChunk(Chunk chunk) {
        // Remove chunk metadata from database
        chunkRepository.delete(chunk);
    }
}