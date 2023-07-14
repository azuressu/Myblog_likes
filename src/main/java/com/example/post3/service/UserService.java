package com.example.post3.service;

import com.example.post3.dto.SignupRequestDto;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.entity.User;
import com.example.post3.entity.UserRoleEnum;
import com.example.post3.jwt.JwtUtil;
import com.example.post3.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public StatusResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        log.info(username);

        String password = passwordEncoder.encode(requestDto.getPassword());

        // username 패턴 확인
        if (Pattern.matches("^[a-z0-9]{4,10}$", username)) {
            // 회원 중복 확인
            Optional<User> checkUsername = userRepository.findByUsername(username);
            if (checkUsername.isPresent()) {
                throw new IllegalArgumentException("중복된 username 입니다.");
            }
        } else {
            throw new IllegalArgumentException("username 구성에 맞지 않습니다. 영어 소문자 및 숫자를 이용해 4자에서 10자 이내로 입력하세요.");
        }

        // password 패턴 확인
        if (!Pattern.matches("^[A-Za-z0-9@$!%*?&!~#]{8,15}$", requestDto.getPassword())){
            throw new IllegalArgumentException("password 구성에 맞지 않습니다. 영어 대소문자 및 숫자 및 특수문자를 이용해 8자에서 15자 이내로 입력하세요.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (!requestDto.getAdminToken().isBlank()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("ADMIN 토큰이 일치하지 않습니다.");
            }
            // 수동으로 admin의 값을 true로 설정해줌
            requestDto.setAdmin(true);
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);

        // 성공했을 경우의 상태코드와 메시지 반환
        StatusResponseDto statusResponseDto = new StatusResponseDto("회원가입 성공", 200);
        return statusResponseDto;
    }

}
