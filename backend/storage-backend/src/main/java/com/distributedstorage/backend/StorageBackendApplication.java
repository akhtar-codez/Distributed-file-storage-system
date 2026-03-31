package com.distributedstorage.backend;

import com.distributedstorage.backend.storage.NodeManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StorageBackendApplication {

    public static void main(String[] args) {
        NodeManager.initializeNodes();
        SpringApplication.run(StorageBackendApplication.class, args);
    }
}