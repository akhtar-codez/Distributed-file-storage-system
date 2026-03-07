
package com.distributedstorage.backend.service;
import java.util.List;
import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.repository.FMDRepository;
import com.distributedstorage.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FileService {

    private final FMDRepository fmdRepository;
    private final UserRepository userRepository;

    public FileService(FMDRepository fmdRepository, UserRepository userRepository){
        this.fmdRepository = fmdRepository;
        this.userRepository = userRepository;
    }

    public FileMetadata saveMetadata(String fileName, String filePath, Long fileSize) {

        // TEMP: always attach file to user with id = 1
        User user = userRepository.findById(1L).orElse(null);

        FileMetadata fileMetadata = new FileMetadata(
                fileName,
                filePath,
                fileSize,
                LocalDateTime.now()
        );

        fileMetadata.setUser(user);

        return fmdRepository.save(fileMetadata);
    }
    public List<FileMetadata> getAllFiles(){
    return fmdRepository.findAll();
}
}