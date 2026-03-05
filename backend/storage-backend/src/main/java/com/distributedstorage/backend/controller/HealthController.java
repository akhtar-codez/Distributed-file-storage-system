package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import com.distributedstorage.backend.dto.HealthResponseDTO;
import com.distributedstorage.backend.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService){
        this.healthService = healthService;
    }

    @GetMapping("/")
    public ApiResponseDTO<HealthResponseDTO> healthCheck(){
        HealthResponseDTO data = healthService.getHealthStatus();

        return new ApiResponseDTO<>("SUCCESS", "Health Check Successful", data);
    }
}
