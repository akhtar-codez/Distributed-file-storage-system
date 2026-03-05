package com.distributedstorage.backend.service;

import org.springframework.stereotype.Service;
import com.distributedstorage.backend.dto.HealthResponseDTO;

@Service
public class HealthService {
    public HealthResponseDTO getHealthStatus() {
        return new HealthResponseDTO("Distributed File Storage Backend Running", "SUCCESS");
    }
}
