package com.distributedstorage.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration.class})
public class StorageBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageBackendApplication.class, args);
	}

}
