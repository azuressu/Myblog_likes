package com.example.post3.security;

import com.example.post3.dto.LoginRequestDto;
import com.example.post3.entity.UserRoleEnum;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals(HttpMethod.POST.name())) { // POST가 아닌 메소드는 걸러내기
            throw new IllegalArgumentException("잘못된 Method 요청입니다.");
        }

        try {
            // LoginRequestDto 사용
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword()
                            ,null
                    )
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

    } // attemptAuthentication()

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 jwt 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        // 성공했을 때는 어떻게 반환해주지 ..? 흠
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        StatusResponseDto statusResponseDto = new StatusResponseDto("로그인 성공", 200);
        new ObjectMapper().writeValue(response.getOutputStream(), statusResponseDto);
    } // successfulAuthentication

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.error("400, 회원을 찾을 수 없습니다.");

        response.setStatus(400);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        StatusResponseDto statusResponseDto = new StatusResponseDto("회원을 찾을 수 없습니다.", 400);
        response.setStatus(400);
        new ObjectMapper().writeValue(response.getOutputStream(), statusResponseDto);

        // 예외 처리를 그냥 던져주면 오류 발생
        // throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
    } // unsuccessfulAuthentication

}