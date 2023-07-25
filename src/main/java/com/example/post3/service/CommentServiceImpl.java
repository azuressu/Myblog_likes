package com.example.post3.service;

import com.example.post3.dto.CommentRequestDto;
import com.example.post3.dto.CommentResponseDto;
import com.example.post3.entity.Comment;
import com.example.post3.entity.CommentLike;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;
import com.example.post3.repository.CommentLikeRepository;
import com.example.post3.repository.CommentRepository;
import com.example.post3.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 작성
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {
        Post post = findPost(id);

        // 작성한 댓글
        Comment comment = new Comment(requestDto, user, post);
        commentRepository.save(comment);

        // 게시글의 댓글 리스트에 댓글 추가하기
        post.addComment(comment);

        return new CommentResponseDto(comment);
    }
    
    // 댓글 수정
    @Override
    @Transactional
    public CommentResponseDto updateCommnet(Long postid, Long commentid, CommentRequestDto requestDto, User user) {
        // 해당 게시글이 있는지 확인
        findPost(postid);
        // 해당 댓글이 있는지 확인
        Comment comment = findComment(commentid);
        // 해당 댓글을 작성한 작성자 이거나, 권한이 ADMIN인 경우 댓글 수정 가능
        if (comment.getUser().getUsername().equals(user.getUsername())
                || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            // 있으면 댓글 내용 업데이트
            comment.update(requestDto);
            // ResponseDto에 내용 담아서 반환
            return new CommentResponseDto(comment);
        } else {
            log.info("작성자만 수정할 수 있습니다.");
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        } // else
    }

    // 댓글 삭제
    public void deleteComment(Long id, Long commentid, User user) {
        // 해당 게시글이 있는지 확인
        findPost(id);
        // 해당 댓글이 있는지 확인
        Comment comment = findComment(commentid);
        // 해당 댓글을 작성한 작성자 이거나, 권한이 ADMIN인 경우 댓글 삭제 가능
        if (comment.getUser().getUsername().equals(user.getUsername())
                || user.getRole().getAuthority().equals("ROLE_ADMIN")) {

            // 댓글의 좋아요 기록도 함께 삭제
            deleteCommentLike(comment);

            // 있으면 댓글 삭제
            commentRepository.delete(comment);
        } else {
            log.info("작성자만 삭제할 수 있습니다.");
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    // 해당 포스트를 찾아서 반환
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다."));
    }
    // 해당 댓글을 찾아서 반환
    public Comment findComment(Long commentid) {
        return commentRepository.findById(commentid).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글은 존재하지 않습니다"));
    }

    // 좋아요 기록 삭제하는 메서드
    public void deleteCommentLike (Comment comment) {
        // 댓글 좋아요 기록 찾아오기
        List<CommentLike> commentLikeList = commentLikeRepository.findByCommentId(comment.getId());
        // 댓글 좋아요 기록 삭제하기
        commentLikeRepository.deleteAll(commentLikeList);
    }

}