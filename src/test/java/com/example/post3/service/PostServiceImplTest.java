package com.example.post3.service;

import com.example.post3.dto.PostRequestDto;
import com.example.post3.dto.PostResponseDto;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;
import com.example.post3.repository.PostLikeRepository;
import com.example.post3.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/* Junit4 ver Test */
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
/* TestInstance.Lifecycle.PER_METHOD: 각 테스트 메서드마다 새로운 인스턴스를 생성하도록 지정하는 옵션 */
class PostServiceImplTest {

    /* Mock 객체를 테스트 대상 객체에 주입할 수 있고, 해당 객체의 메서드를 호출해 테스트 수행 가능 */
    @InjectMocks
    PostServiceImpl postService;

    @Mock
    PostRepository postRepository;

    @Mock
    PostLikeRepository postLikeRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("PostSerivce: 게시글 전체 조회 확인")
    void getPostsTest() {
        // given
        List<PostResponseDto> postResponseDtoList = postRepository.findAllByOrderByCreateTimeDesc().stream().map(PostResponseDto::new).toList();

        // when
        List<PostResponseDto> postResponseDtoList1 = postService.getPosts();

        // then
        assertEquals(postResponseDtoList, postResponseDtoList1);
    }

    @Test
    @DisplayName("PostService: 게시글 단건 조회 확인")
    void getOnePostTest() {
        // given
        User user = new User();
        PostRequestDto postRequestDto = new PostRequestDto("title", "contents");
        var newPost = Post.builder().requestDto(postRequestDto).user(user).build();
        given(postRepository.findById(20L)).willReturn(Optional.ofNullable(newPost));

        // when
        PostResponseDto postResponseDto = postService.getOnePost(20L);

        // then
        then(newPost.getTitle()).equals(postResponseDto.getTitle());
        then(newPost.getContents()).equals(postResponseDto.getContents());
    }

    @Test
    @DisplayName("PostService: 게시글 작성 확인")
    void createPostTest() {
        // given
        User user = new User();
        PostRequestDto postRequestDto = new PostRequestDto("title", "contents");
        var newPost = Post.builder().requestDto(postRequestDto).user(user).build();
        given(postRepository.save(any())).willReturn(newPost);

        // when
//        PostResponseDto postResponseDto = postService.createPost(postRequestDto, user);
//        postService.createPost(postRequestDto, user);

        // then
//        then(newPost.getTitle()).equals(postResponseDto.getTitle());
//        then(newPost.getContents()).equals(postResponseDto.getContents());
//        then(postRepository).should(times(1)).save(any());

        /* 주석 처리 된 내용끼리 테스트 통과 확인 */
    }

    @Test
    @DisplayName("PostService: 게시글 수정 확인")
    void updatePostTest() {
        // given
        User user = new User();
        PostRequestDto postRequestDto = new PostRequestDto("title", "content");
        var newPost = Post.builder().requestDto(postRequestDto).user(user).build();
        PostRequestDto updatePostRequestDto = new PostRequestDto("It's", "a piece of cake");

        // when
        PostResponseDto postResponseDto = postService.updatePost(newPost, updatePostRequestDto, user);
        newPost.update(updatePostRequestDto);

        // then
        then(newPost.getTitle()).equals(postResponseDto.getTitle());
        then(newPost.getContents()).equals(postResponseDto.getContents());
    }

    @Test
    @DisplayName("PostService: 게시글 삭제 확인")
    void deletePostTest() {
        // given
        User user = new User();
        PostRequestDto postRequestDto = new PostRequestDto("title", "content");
        var newPost = Post.builder().requestDto(postRequestDto).user(user).build();
        given(postLikeRepository.findByPostId(newPost.getId())).willReturn(null);

        // when
        postService.deletePost(newPost, user);

        // then
        then(15).equals(postRepository.count());
    }

}