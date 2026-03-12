package com.distributedstorage.backend.exception;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponseDTO<?> handleResourceNotFound(ResourceNotFoundException ex){

        return new ApiResponseDTO<>("ERROR",ex.getMessage(),null);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponseDTO<?> handleGeneralException(Exception ex){
        return new ApiResponseDTO<>("ERROR","Something went wrong",  null);
    }
}
