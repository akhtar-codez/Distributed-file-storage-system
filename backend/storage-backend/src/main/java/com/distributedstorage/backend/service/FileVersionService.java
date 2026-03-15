package com.distributedstorage.backend.service;

import com.distributedstorage.backend.model.FileVersion;
import com.distributedstorage.backend.repository.FileVersionRepository;
import org.springframework.stereotype.Service;

@Service
public class FileVersionService {

    private final FileVersionRepository fileVersionRepository;

    public FileVersionService(FileVersionRepository fileVersionRepository) {
        this.fileVersionRepository = fileVersionRepository;
    }

    public FileVersion saveVersion(FileVersion fileVersion){
        return fileVersionRepository.save(fileVersion);
    }
}