package com.example.post3.exception;

import com.sun.jdi.request.DuplicateRequestException;
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

    @ExceptionHandler({DuplicateRequestException.class})
    public ResponseEntity<StatusResponseDto> duplicateRequestException(DuplicateRequestException e) {
        StatusResponseDto statusResponseDto = new StatusResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // Http Body
                statusResponseDto,
                // Http Status Code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<StatusResponseDto> nullPointerExceptionHandler(NullPointerException e) {
        StatusResponseDto statusResponseDto = new StatusResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(
                statusResponseDto,
                HttpStatus.NOT_FOUND
        );
    }
}
