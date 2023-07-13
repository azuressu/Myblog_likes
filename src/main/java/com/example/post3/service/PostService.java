package com.example.post3.service;

import com.example.post3.dto.PostRequestDto;
import com.example.post3.dto.PostResponseDto;
import com.example.post3.entity.Post;
import com.example.post3.entity.PostLike;
import com.example.post3.entity.User;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.repository.CommentRepository;
import com.example.post3.repository.PostLikeRepository;
import com.example.post3.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final CommentRepository commentRepository;

    private final PostLikeRepository postLikeRepository;

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
            log.error("게시글 작성자만 수정할 수 있습니다.");
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    public StatusResponseDto deletePost(Long id, User user) {
        Post post = findPost(id);
        // 해당 게시글을 작성한 작성자 이거나, 권한이 ADMIN인 경우는 삭제 가능
        if (user.getUsername().equals(post.getUser().getUsername())
                || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            // 게시글의 좋아요 기록 삭제
            deletePostLike(post);
            
            // 게시글 삭제
            postRepository.delete(post);

            StatusResponseDto statusResponseDto = new StatusResponseDto("게시글 삭제 성공", 200);
            return statusResponseDto;
        } else {
            log.error("게시글 작성자만 삭제할 수 있습니다.");
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    // 해당 포스트를 찾아서 반환
    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다."));
    }

    // 게시글 좋아요 기록 삭제하기
    private void deletePostLike(Post post) {
        // 게시글 좋아요 기록 가져오기
        List<PostLike> postLikeList = postLikeRepository.findByPost(post);
        // 게시글 좋아요 기록 삭제하기
        postLikeRepository.deleteAll(postLikeList);
    }

}
