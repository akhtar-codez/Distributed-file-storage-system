package com.distributedstorage.backend.service;

import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.model.FileVersion;
import com.distributedstorage.backend.repository.FileVersionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FileVersionService {

    private final FileVersionRepository fileVersionRepository;

    public FileVersionService(FileVersionRepository fileVersionRepository) {
        this.fileVersionRepository = fileVersionRepository;
    }

    // Save a version directly (existing functionality)
    public FileVersion saveVersion(FileVersion fileVersion){
        return fileVersionRepository.save(fileVersion);
    }

    // This method automatically creates the next version of a file
    public FileVersion createNewVersion(FileMetadata fileMetadata){

        // Fetch the latest version for the given file
        Optional<FileVersion> latestVersion =
                fileVersionRepository.findTopByFileOrderByVersionNumberDesc(fileMetadata);

        int newVersionNumber;

        // If the file already has versions, increment the latest version
        if(latestVersion.isPresent()){
            newVersionNumber = latestVersion.get().getVersionNumber() + 1;
        }
        // If no version exists yet, start with version 1
        else{
            newVersionNumber = 1;
        }

        // Create new FileVersion object
        FileVersion newVersion = new FileVersion();

        // Set version number
        newVersion.setVersionNumber(newVersionNumber);

        // Set timestamp when version is created
        newVersion.setCreatedAt(LocalDateTime.now());

        // Link version to the file metadata
        newVersion.setFile(fileMetadata);

        // Save the new version in the database
        return fileVersionRepository.save(newVersion);
    }
    public List<FileVersion> getVersionsByFile(FileMetadata file){
    return fileVersionRepository.findByFile(file);
}
}