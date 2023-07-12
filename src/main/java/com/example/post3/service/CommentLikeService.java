package com.example.post3.service;

import com.example.post3.dto.LikeRequestDto;
import com.example.post3.entity.Comment;
import com.example.post3.entity.CommentLike;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;
import com.example.post3.repository.CommentLikeRepository;
import com.example.post3.repository.CommentRepository;
import com.example.post3.repository.PostRepository;
import com.example.post3.security.UserDetailsImpl;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    public ResponseEntity<String> addcommentlike(Long postId, Long commentId, UserDetailsImpl userDetails, LikeRequestDto likeRequestDto) {
        Post post = findPost(postId);
        Comment comment = findComment(commentId);
        User user = userDetails.getUser();

        if (likeRequestDto.isLike()) {
            if (commentLikeRepository.findByUserAndPostAndComment(user, post, comment).isPresent()) {
                log.info("좋아요가 이미 눌려있음");
                throw new DuplicateRequestException("좋아요가 이미 눌러져 있습니다.");
            } else {
                log.info("좋아요 누르기 시도중");
                CommentLike commentLike = new CommentLike(user, post, comment);
                commentLikeRepository.save(commentLike);

                // 해당 댓글의 like + 1
                comment.setLikeCount(comment.getLikeCount() + 1);
                commentRepository.save(comment);
            }
            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.badRequest().body("error");
        }
    }

    public ResponseEntity<String> deletecommentlike(Long postId, Long commentId, UserDetailsImpl userDetails, LikeRequestDto likeRequestDto) {
        Post post = findPost(postId);
        Comment comment = findComment(commentId);
        User user = userDetails.getUser();

        if (!likeRequestDto.isLike()) {
            if (commentLikeRepository.findByUserAndPostAndComment(user, post, comment).isPresent()) {
                if (user.equals(commentLikeRepository.findByUserAndPostAndComment(user, post, comment))) {
                    CommentLike commentLike = commentLikeRepository.findByUserAndPostAndComment(user, post, comment).orElseThrow(() -> new IllegalArgumentException("이 게시글에 좋아요가 눌러져 있지 않습니다."));
                    commentLikeRepository.delete(commentLike);

                    // 해당 게시글의 like - 1
                    comment.setLikeCount(comment.getLikeCount() - 1);
                    commentRepository.save(comment);
                }
            }
            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.badRequest().body("error");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }


}
