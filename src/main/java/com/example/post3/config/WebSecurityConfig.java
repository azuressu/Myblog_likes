package com.example.post3.config;

import com.example.post3.jwt.JwtUtil;
import com.example.post3.security.JwtAuthenticationFilter;
import com.example.post3.security.JwtAuthorizationFilter;
import com.example.post3.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 자원 사용을 가능하게 하는 에너테이션
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 인터페이스라 객체가 없어서 받아올 수 없기 때문에, 수동으로 등록하여 BCryptPasswordEncoder를 사용할 수 있도록 하는 것
    // 수동 등록을 해야 사용할 수 있기 때문에 등록을 꼭 해주도록 하자.
    // spring에 있는 interface를 사용하려면 bean으로 등록을 해줘야 한다.

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        // return 인증
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    // https://ojt90902.tistory.com/843
    // https://bitgadak.tistory.com/11
    // https://gngsn.tistory.com/160
    // https://catsbi.oopy.io/c0a4f395-24b2-44e5-8eeb-275d19e2a536
    // https://velog.io/@soyeon207/Spring-Filter-Interceptor-AOP

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // 프록시 레이어를 감싸고 있다 그래서 프록시 안에 세큐리티 필터들이 묶음으로 관리되고 있다
        // CSRF 설정 (csrf:(Cross-Site Request Forgery) 방어 기능 비활성화)
        http.csrf((csrf) -> csrf.disable());
//        http.httpBasic((basic) -> basic.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement)
                -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((authorizeHttpRequests) -> // 이 코드 자체가 인가를 처리하는 부분
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/").permitAll() // 메인 페이지 요청
                        .requestMatchers("/api/user/**").permitAll() // "/api/user/" 로 시작하는 요청 모두 접근 허가
                        .requestMatchers(HttpMethod.GET, "/api/posts").permitAll() // "/api/posts" 만 허용
                        .requestMatchers(HttpMethod.GET,"/api/post/**").permitAll() // 해당 url의 get 요청만 허용
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        // Filter를 거쳐서 권한을 부여받고 그 권한을 Http.authorizeHttpReques 코드에서 해당 url에 따른 권한을 매칭시켜 확인
        // permitAll(): 권한에 상관없이 접근이 가능

        // form 로그인 사용하지 않음
        http.formLogin((formLogin) -> formLogin.disable());

        // 필터 관리 - 지정된 필터 앞에 커스텀 필터를 추가
        // filterchain 자체가 filter들을 묶어서 관리한다
//        http.addFilterBefore(jwtExceptionFilter(), JwtExceptionFilter.class);
//        http.addFilterBefore(커스텀필터, JwtAuthorizationFilter.class);
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
