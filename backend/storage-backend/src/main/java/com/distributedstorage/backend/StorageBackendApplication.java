package com.distributedstorage.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class StorageBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageBackendApplication.class, args);
	}

}
