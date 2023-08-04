package com.example.post3.service;

import com.example.post3.dto.PostRequestDto;
import com.example.post3.dto.PostResponseDto;
import com.example.post3.entity.Post;
import com.example.post3.entity.PostLike;
import com.example.post3.entity.User;
import com.example.post3.exception.NotFoundException;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.repository.CommentRepository;
import com.example.post3.repository.PostLikeRepository;
import com.example.post3.repository.PostRepository;
import com.example.post3.s3.S3Uploader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    @Autowired
    private final S3Uploader s3Uploader;

    @Override
    public PostResponseDto createPost(String requestDto, User user, MultipartFile multipartFile) throws IOException, JsonProcessingException {
        PostRequestDto postRequestDto = conversionDto(requestDto);

        // Post 생성자로 게시글의 제목, 내용, 사용자를 설정
        Post post = new Post(postRequestDto, user);

        // 이미지가 비어있지 않다면
        if (!multipartFile.isEmpty()) {
            String storedFileName = s3Uploader.upload(multipartFile);
            post.setImageUrl(storedFileName);
        }

        // DB에 저장
        Post savePost = postRepository.save(post);
        return new PostResponseDto(savePost);
    }

    @Override
    public List<PostResponseDto> getPosts() {
        List<PostResponseDto> postResponseDtoList = postRepository.findAllByOrderByCreateTimeDesc().stream().map(PostResponseDto::new).toList();
        return postResponseDtoList;
    }

    public PostResponseDto getOnePost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Post post, PostRequestDto requestDto, User user){
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    public void deletePost(Post post, User user) {
        // 게시글의 좋아요 기록 삭제
        deletePostLike(post);
        // 게시글 삭제
        postRepository.delete(post);
    }

    // 해당 포스트를 찾아서 반환
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new NotFoundException("선택한 게시글은 존재하지 않습니다."));
    }

    // 게시글 좋아요 기록 삭제하기
    public void deletePostLike(Post post) {
        // 게시글 좋아요 기록 가져오기
        List<PostLike> postLikeList = postLikeRepository.findByPostId(post.getId());
        // 게시글 좋아요 기록 삭제하기
        postLikeRepository.deleteAll(postLikeList);
    }

    // String 타입의 형태를 다시 PostRequestDto 형태로 바꿈
    public PostRequestDto conversionDto(String requestDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(requestDto, PostRequestDto.class);
    }

}
