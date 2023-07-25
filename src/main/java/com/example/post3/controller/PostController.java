package com.example.post3.controller;

import com.example.post3.dto.PostRequestDto;
import com.example.post3.dto.PostResponseDto;
import com.example.post3.entity.Post;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.security.UserDetailsImpl;
import com.example.post3.service.PostServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // ResponseBody는 붙이면 안됨 !
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    // 제어의 흐름 : PostController → PostService → PostRepository
    private final PostServiceImpl postService;

    // 게시글 작성
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }

    // 전체 게시글 조회 (Token 상관 없음 - WebSecurityConfig에서 설정)
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    // 선택 게시글 조회 (Token 상관 없음 - WebSecurityConfig에서 설정)
    @GetMapping("/post/{id}")
    public PostResponseDto getOnePost(@PathVariable Long id) {
        return postService.getOnePost(id);
    }

    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postService.findPost(id);
        return postService.updatePost(post, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/post/{id}")
    public StatusResponseDto deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Post post = postService.findPost(id);
        postService.deletePost(post, userDetails.getUser());
        return new StatusResponseDto("게시글 삭제 성공", 200);
    }

}