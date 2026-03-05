package com.distributedstorage.backend.dto;

public class HealthResponseDTO {
    private String message;
    private String status;

    public HealthResponseDTO(String message, String status){
        this.message = message;
        this.status = status;
    }

    public String getMessage(){
        return message;
    }
    public String getStatus(){
        return status;
    }
}
