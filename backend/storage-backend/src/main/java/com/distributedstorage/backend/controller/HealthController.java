package com.distributedstorage.backend.controller;

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
    public String healthCheck() {
        return healthService.getHealthStatus();
    }
}
