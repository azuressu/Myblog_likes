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

@Slf4j
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public StatusResponseDto signup(HttpServletResponse res, SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        log.info(username);

        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            try {
                status(400, "중복된 username 입니다.", res);
                log.error("중복된 username 입니다.");
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//            throw new MyBlogException(MyBlogErrorCode.IN_USED_USERNAME, null);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (!requestDto.getAdminToken().isBlank()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("권한 잘못됨");
            }
            // 수동으로 admin의 값을 true로 설정해줌
            requestDto.setAdmin(true);
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);

        StatusResponseDto statusResponseDto = new StatusResponseDto();
        statusResponseDto.setMessage("회원가입 성공");
        statusResponseDto.setStatusCode(200);

        return statusResponseDto;
    }

    // 상태 코드 반환하기
    public void status(int statusCode, String message, HttpServletResponse response) throws IOException {
        // 응답 데이터를 JSON 형식으로 생성
        String jsonResponse = "{\"status\": " + statusCode + ", \"message\": \"" + message + "\"}";

        // Content-Type 및 문자 인코딩 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // PrintWriter를 사용하여 응답 데이터 전송
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }
}
