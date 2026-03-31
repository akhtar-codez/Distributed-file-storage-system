package com.distributedstorage.backend;

import com.distributedstorage.backend.storage.NodeManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class StorageBackendApplication {

    public static void main(String[] args) {
        NodeManager.initializeNodes();
        SpringApplication.run(StorageBackendApplication.class, args);
    }
}