package com.distributedstorage.backend.dto;

public class ApiResponseDTO<T> {
    private String status;
    private String message;
    private T data;

    public ApiResponseDTO(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
