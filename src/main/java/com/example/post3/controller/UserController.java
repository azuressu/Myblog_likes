package com.example.post3.controller;

import com.example.post3.dto.SignupRequestDto;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/signup")
    public StatusResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }
    /* sign up 할 때, HttpservletResponse res, (res.setStatus), SignupRequestDto 를 매개변수로
     * 받아와서, 중복 확인하고 (Service로 넘겨라)
     * 맞으면, return 상태값 & 메세지 */
}