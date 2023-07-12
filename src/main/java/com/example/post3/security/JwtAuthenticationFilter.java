package com.example.post3.security;

import com.example.post3.dto.LoginRequestDto;
import com.example.post3.entity.UserRoleEnum;
import com.example.post3.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

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
            try {
                status(400, "Http Method Error", response);
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
            try {
                status(400, "회원을 찾을 수 없습니다.", response);
                log.error("회원을 찾을 수 없습니다.");
                return null;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
//            throw new RuntimeException(e.getMessage());
//            throw new MyBlogException(MyBlogErrorCode.NOT_FOUND_USER, null);
        }
    } // attemptAuthentication()

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 jwt 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        status(200, "로그인 성공", response);
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(400);
        status(400, "회원을 찾을 수 없습니다.", response);
        log.error("400, 회원을 찾을 수 없습니다.");
//        throw new MyBlogException(MyBlogErrorCode.NOT_FOUND_USER, null);
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