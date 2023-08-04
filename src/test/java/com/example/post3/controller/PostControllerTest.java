package com.example.post3.controller;

import com.example.post3.dto.PostRequestDto;
import com.example.post3.entity.UserRoleEnum;
import com.example.post3.jwt.JwtUtil;
import com.example.post3.security.UserDetailsImpl;
import com.example.post3.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WebApplicationContext context;

    String token;

    @BeforeEach
    @DisplayName("user 로그인해서 UserDetails로")
    public void setUp() throws Exception{
        /* MockMvcBuilders: MockMvc를 생성하는 빌더 클래스 - MockMvc 객체를 설정하고 빌드할 수 있음
        *  webAppContextSetup(context): MockMvc를 설정하기 위해서 사용할 웹 어플리케이션 컨텍스트(애플리케이션에 대한 구성 정보를 제공하는 스프링의 핵심 컨테이너)를 지정
        *  apply(springSecurity()): Spring Security를 적용해 MockMvc를 설정(테스트 중에도 Security 인증 및 권한 검사가 가능)
        *  build(): 모든 옵션을 기반으로 MockMvc 객체를 빌드해 반환. 이렇게 생성된 MockMvc 객체를 통해 컨트롤러 테스트 및 Http 요청 및 응답 검증 가능*/

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // 이미 존재하는 회원으로 token 생성
        // username: username, role:UserRoleEnum.USER
        token = jwtUtil.createToken("username", UserRoleEnum.USER);
    }

    @Test
//    @WithUserDetails("username")
    @DisplayName("PostController: 게시글 작성 확인 (POST)")
    void createPost() throws Exception{
        // given
        String title = "테스트 입니닷";
        String contents = "Testing 중이에요";

        String postRequestDto = objectMapper.writeValueAsString(new PostRequestDto(title, contents));

        mockMvc.perform(post("/api/post")
                        .header("Authorization", token)
                        .content(postRequestDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PostController: 게시글 전체 조회 확인 (GET)")
    // 로그인 없이도 테스트 통과 확인 완료
    void getPosts() throws Exception {
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PostController: 게시글 선택 조회 확인 (GET)")
    // 로그인 없이도 테스트 통과 확인 완료
    void getOnePost() throws Exception {
        mockMvc.perform(get("/api/post/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PostController: 게시글 수정 확인 (PUT)")
    void updatePost() throws Exception {
        String title = "지금우린마치";
        String contents = "12시30분의 시계바늘처럼";

        String postRequestDto = objectMapper.writeValueAsString(new PostRequestDto(title, contents));

        mockMvc.perform(put("/api/post/1")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequestDto))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PostController: 게시글 삭제 확인 (DELETE)")
    void deletePost() throws Exception {
        mockMvc.perform(delete("/api/post/18")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }


}