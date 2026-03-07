package com.distributedstorage.backend.service;

import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.repository.FMDRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class FileService {
    
    private final FMDRepository fmdRepository;

    public FileService(FMDRepository fmdRepository){
        this.fmdRepository = fmdRepository;
    }

    public FileMetadata saveMetadata(String fileName, String filePath, Long fileSize) {
        FileMetadata fileMetadata = new FileMetadata(
                fileName,
                filePath,
                fileSize,
                LocalDateTime.now()
        );
        return fmdRepository.save(fileMetadata);
    }
}
