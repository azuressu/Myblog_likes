package com.example.post3.security;

import com.example.post3.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final MessageSource messageSource;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getJwtFromHeader(req); // 상태코드 반환을 위해서 res 추가

        if (StringUtils.hasText(tokenValue) && !req.getMethod().equals("GET")) {

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error: HttpServletRequest Header의 토큰이 유효하지 않습니다.");
                throw new IllegalArgumentException();
//                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    } // doFilterInternal

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    } // setAuthentication

    // 인증 객체 생성
    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // return 타입이 Authentication 임
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    } // createAuthentication()

    // 상태 코드 반환하기
    public void status(int statusCode, String message, HttpServletResponse response) throws IOException {
        // 응답 데이터를 JSON 형식으로 생성
        String jsonResponse = "{\"statusCode\": " + statusCode + ", \"message\": \"" + message + "\"}";

        // Content-Type 및 문자 인코딩 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // PrintWriter를 사용하여 응답 데이터 전송
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }

}