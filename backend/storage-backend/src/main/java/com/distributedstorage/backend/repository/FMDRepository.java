package com.distributedstorage.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.distributedstorage.backend.model.FileMetadata;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FMDRepository extends JpaRepository<FileMetadata, Long>{
    List<FileMetadata> findByUserId(Long userId);
}