package com.distributedstorage.backend.repository;

import com.distributedstorage.backend.model.FileVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileVersionRepository extends JpaRepository<FileVersion, Long> {
}