package com.example.post3.controller;

import com.example.post3.dto.SignupRequestDto;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public StatusResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // Validation 예외 처리
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<StatusResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StatusResponseDto statusResponseDto = new StatusResponseDto(e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // Http Body
                statusResponseDto,
                // Http StatusCode
                HttpStatus.BAD_REQUEST
        );
    }
}