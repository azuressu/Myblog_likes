package com.example.post3.jwt;

import com.example.post3.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component // Bean으로 등록할 에너테이션
public class JwtUtil {

    // Header Key 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 Key
    public static final String AUTHORIZAION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // Token 만료 시간
    public final Long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /* 필터가 생성될 때 수행되는 메소드
    *  웹 컨테이너가 사용 중인 필터를 나타내기 위해 호출
    *  서블릿 컨테이너 틀 필터를 인스턴스화 한 후에 init 메서드를 정확하게 한 번 호출
    *  필터가 필터링 작업을 수행하도록 요쳥하기 전에 init 메서드는 성공적으로 완료되어야 함*/
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Token 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값 (사용자 이름)
                        .claim(AUTHORIZAION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 토큰 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    } // createToken()

    // Header에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        // Header에서 토큰 뽑기
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // 토큰이 null이 아니면서 비어있지 않고, Bearer로 시작하는지 확인 후
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            // 7번째 부터 잘라서 가져오기
            return bearerToken.substring(7);
        }
        return null;
    }

    // Token 검증
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Token 검증 실패");
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Token 검증 실패");
            log.error("Expired JWT token, 만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Token 검증 실패");
            log.error("Unsupported JWT token, 지원되지 않은 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("Token 검증 실패");
            log.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }
        return false;
    }

    // Token 에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}
