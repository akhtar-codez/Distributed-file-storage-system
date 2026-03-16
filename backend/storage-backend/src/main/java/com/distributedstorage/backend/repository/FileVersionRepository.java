package com.distributedstorage.backend.repository;
import java.util.List;
import com.distributedstorage.backend.model.FileVersion;
import com.distributedstorage.backend.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileVersionRepository extends JpaRepository<FileVersion, Long> {

    // This method fetches the latest version of a file
    // Spring automatically generates the query based on the method name
    // It finds the record with highest versionNumber for a given file
    Optional<FileVersion> findTopByFileOrderByVersionNumberDesc(FileMetadata file);


    List<FileVersion> findByFile(FileMetadata file);


}