package com.distributedstorage.backend.exception;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles ResourceNotFoundException — returns 404 Not Found
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponseDTO<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ApiResponseDTO<>("ERROR", ex.getMessage(), null);
    }

    // Handles BadRequestException — returns 400 Bad Request
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiResponseDTO<?> handleBadRequest(BadRequestException ex) {
        return new ApiResponseDTO<>("ERROR", ex.getMessage(), null);
    }

    // Handles @Valid annotation failures — returns 400 Bad Request
    // Collects all field validation errors into a single message
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseDTO<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        // Extract all field error messages and join them
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return new ApiResponseDTO<>("ERROR", errors, null);
    }

    // Handles all other unhandled exceptions — returns 500 Internal Server Error
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponseDTO<?> handleGeneralException(Exception ex) {
        return new ApiResponseDTO<>("ERROR", ex.getMessage(), null);
    }
}