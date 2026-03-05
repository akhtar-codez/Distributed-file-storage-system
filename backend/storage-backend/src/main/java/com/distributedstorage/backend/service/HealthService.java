package com.distributedstorage.backend.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {
    public String getHealthStatus(){
        return "Distributed File Storage Backend Running";
    }
}
