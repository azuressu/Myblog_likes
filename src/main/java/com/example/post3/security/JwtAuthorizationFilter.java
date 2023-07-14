package com.example.post3.security;

import com.example.post3.exception.StatusResponseDto;
import com.example.post3.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    /* request, response가 필터를 지나갈 때 수행하는 메서드
    *  클라이언트의 요청이나 응답으로 인해서 chain을 통과할 때마다 컨테이너에 의해 호출되는 메소드
    *  이 메소드에 파라미터로 전달된 필터 체인은 필터가 요청과 응답을 다음 엔티티로 전달할 수 있도록 함
    *  https://okky.kr/questions/1418734*/

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        log.info(req.getMethod() + ", " + req.getRequestURI());
        log.info(String.valueOf(req.getRequestURI().startsWith("/api/post")));

        if (! (req.getRequestURI().startsWith("/api/posts") ||
                (req.getMethod().equals("GET") && req.getRequestURI().startsWith("/api/post")) ||
                (req.getMethod().equals("POST") && req.getRequestURI().startsWith("/api/user")))) {
            String tokenValue = jwtUtil.getJwtFromHeader(req);

            if (tokenValue != null) { // hasText() 하니까 안되고, != null 된다 ..
                if (!jwtUtil.validateToken(tokenValue)) { // 토큰의 유효성을 검사함
                    log.info("Token Error: HttpServletRequest Header의 토큰이 유효하지 않습니다.");
                    StatusResponseDto statusResponseDto = new StatusResponseDto("토큰이 유효하지 않습니다.", 400);
                    res.setStatus(400);
                    new ObjectMapper().writeValue(res.getOutputStream(), statusResponseDto);
                }
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                setAuthentication(info.getSubject());
            } else {
                StatusResponseDto statusResponseDto = new StatusResponseDto("토큰이 비어 있습니다.", 400);
                res.setStatus(400);
                new ObjectMapper().writeValue(res.getOutputStream(), statusResponseDto);
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
}