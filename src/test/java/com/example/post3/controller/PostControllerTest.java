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
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    WebApplicationContext context;

    String token;

    @BeforeEach
    @DisplayName("user 로그인해서 UserDetails로")
    public void setUp() throws Exception{
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // 이미 있는 회원정보로 UserDetailsImpl을 생성함
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("username");
        // 해당 UserDetailsImpl에서 username과 role을 가져옴
        String username = userDetails.getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        // username과 role로 token을 생성함
        token = jwtUtil.createToken(username, role);
    }

    @Test
//    @WithUserDetails("username")
    @DisplayName("PostController: 게시글 작성 확인 (POST)")
    void createPost() throws Exception{
        // given
        String title = "Testing";
        String contents = "Testing ~";

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
        mockMvc.perform(delete("/api/post/13")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }


}