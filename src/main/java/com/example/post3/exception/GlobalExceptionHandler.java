package com.example.post3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<StatusResponseDto> handleException(IllegalArgumentException e) {
        StatusResponseDto statusResponseDto = new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // Http Body
                statusResponseDto,
                // Http Status Code
                HttpStatus.BAD_REQUEST
        );
    }
}
