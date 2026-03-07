package com.distributedstorage.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.distributedstorage.backend.model.FileMetadata;
import org.springframework.stereotype.Repository;

@Repository
public interface FMDRepository extends JpaRepository<FileMetadata, Long>{
    
}
