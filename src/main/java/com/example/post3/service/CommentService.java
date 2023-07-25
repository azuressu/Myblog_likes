package com.example.post3.service;

import com.example.post3.dto.CommentRequestDto;
import com.example.post3.dto.CommentResponseDto;
import com.example.post3.entity.Comment;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;

public interface CommentService {

    /**
     * 댓글 작성
     * @param postid: 댓글을 작성할 게시글의 id
     * @param requestDto: 댓글 내용
     * @param user: 댓글 작성 요청자
     * @return: 작성한 댓글 내용
     */
    CommentResponseDto createComment(Long postid, CommentRequestDto requestDto, User user);

    /**
     * 댓글 수정
     * @param postid: 수정할 댓글의 게시글 id
     * @param comment: 수정할 댓글
     * @param requestDto: 수정할 댓글의 내용
     * @param user: 댓글 수정 요청자
     * @return: 수정한 댓글 내용
     */
    CommentResponseDto updateCommnet(Long postid, Comment comment, CommentRequestDto requestDto, User user);

    /**
     * 댓글 삭제
     * @param postid: 삭제할 댓글의 게시글 id
     * @param comment: 삭제할 댓글
     * @param user: 댓글 삭제 요청자
     */
    void deleteComment(Long postid, Comment comment, User user);

    /**
     * 게시글 찾기
     * @param postid: 찾을 게시글 id
     * @return: 찾은 게시글 내용
     */
    Post findPost(Long postid);

    /**
     * 댓글 찾기
     * @param commentid: 찾을 댓글 id
     * @return: 찾은 댓글 내용
     */
    Comment findComment(Long commentid);

    /**
     * 댓글 좋아요 삭제
     * @param comment: 좋아요를 삭제할 댓글
     */
    void deleteCommentLike(Comment comment);
}
