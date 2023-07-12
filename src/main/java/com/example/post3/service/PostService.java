package com.example.post3.service;

import com.example.post3.dto.PostRequestDto;
import com.example.post3.dto.PostResponseDto;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;
import com.example.post3.repository.CommentRepository;
import com.example.post3.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post();
        post.setUser(user);
        post.setTitle(requestDto.getTitle());
        post.setContents(requestDto.getContents());

        // DB에 저장
        Post savePost = postRepository.save(post);
        PostResponseDto postResponseDto = new PostResponseDto(savePost);
        return postResponseDto;
    }

    public List<PostResponseDto> getPosts() {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        List<Post> postList = postRepository.findAllByOrderByCreateTimeDesc();

        for (Post post: postList) {
            post.setCommentList(commentRepository.findAllByPostIdOrderByCreateTimeDesc(post.getId()));
            postResponseDtoList.add(new PostResponseDto(post));
        }
        return postResponseDtoList;
    }

    public PostResponseDto getOnePost(Long id) {
        Post post = findPost(id);
        post.setCommentList(commentRepository.findAllByPostIdOrderByCreateTimeDesc(post.getId()));
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user, HttpServletResponse res){
        Post post = findPost(id);
        // 해당 게시글을 작성한 작성자 이거나, 권한이 ADMIN인 경우는 삭제 가능
        if (post.getUser().getUsername().equals(user.getUsername())
                || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            post.update(requestDto);
            PostResponseDto postResponseDto = new PostResponseDto(post);
            return postResponseDto;
        } else {
            try {
                log.error("게시글 작성자만 수정할 수 있습니다.");
                status(400, "작성자만 삭제/수정할 수 있습니다.", res);
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public StatusResponseDto deletePost(Long id, User user) {
        Post post = findPost(id);
        // 해당 게시글을 작성한 작성자 이거나, 권한이 ADMIN인 경우는 삭제 가능
        if (user.getUsername().equals(post.getUser().getUsername())
                || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            postRepository.delete(post);

            StatusResponseDto statusResponseDto = new StatusResponseDto();
            statusResponseDto.setMessage("게시글 삭제 성공");
            statusResponseDto.setStatusCode(200);
            return statusResponseDto;
        } else {
            StatusResponseDto statusResponseDto = new StatusResponseDto();
            log.error("게시글 작성자만 수정할 수 있습니다.");
            statusResponseDto.setMessage("작성자만 삭제/수정할 수 있습니다.");
            statusResponseDto.setStatusCode(400);
            return statusResponseDto;
        }
    }

    // 해당 포스트를 찾아서 반환
    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다."));
    }

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
