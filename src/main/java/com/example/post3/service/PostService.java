package com.example.post3.service;

import com.example.post3.dto.PostRequestDto;
import com.example.post3.dto.PostResponseDto;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;
import com.example.post3.exception.StatusResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    /**
     * 게시글 작성
     * @param requestDto: 게시글 작성 시 요청 정보
     * @param user: 게시글 작성 요청자
     * @return: 게시글 작성 결과
     */
    PostResponseDto createPost(String requestDto, User user, MultipartFile multipartFile) throws IOException;

    /**
     * 게시글 전체 목록 조회
     * @return: 전체 게시글 목록
     */
    List<PostResponseDto> getPosts();

    /**
     * 게시글 한 건 조회
     * @param id: 조회하고자 하는 게시글 id
     * @return: 조회된 게시글
     */
    PostResponseDto getOnePost(Long id);

    /**
     * 게시글 수정
     * @param post: 수정할 게시글
     * @param requestDto: 수정할 게시글의 내용
     * @param user: 게시글 수정 요청자
     * @return: 수정된 게시글 정보
     */
    PostResponseDto updatePost(Post post,PostRequestDto requestDto, User user);

    /**
     * 게시글 삭제
     * @param post: 삭제할 게시글
     * @param user: 게시글 삭제 요청자
     */
    void deletePost(Post post, User user);

    /**
     * 게시글 찾기
     * @param id: 찾을 게시글 id
     * @return: 찾은 게시글 정보
     */
    Post findPost(Long id);

    /**
     * 게시글 좋아요 삭제
     * @param post: 좋아요를 삭제할 게시글 정보
     */
    void deletePostLike(Post post);

}
