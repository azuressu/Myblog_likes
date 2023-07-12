package com.example.post3.service;

import com.example.post3.entity.Comment;
import com.example.post3.entity.CommentLike;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;
import com.example.post3.repository.CommentLikeRepository;
import com.example.post3.repository.CommentRepository;
import com.example.post3.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseEntity<String> addcommentlike(Long postId, Long commentId, User user) {
        Post post = findPost(postId);
        Comment comment = findComment(commentId);

        Optional<CommentLike> commentLike = findCommentLike(user, post, comment);

        if (commentLike.isPresent()) {
            if (commentLike.get().getLike()) { // true 라면~
                throw new IllegalArgumentException("댓글에 이미 좋아요가 눌러져 있습니다.");
            } else {
                commentLike.get().setLike(true);

                // 해당 댓글의 좋아요 수 + 1
                comment.setLikeCount(comment.getLikeCount() + 1);
            }
        } else {
            CommentLike newcommentLike = new CommentLike(user, post, comment);
            newcommentLike.setLike(true);
            commentLikeRepository.save(newcommentLike);

            // 해당 댓글의 좋아요 수 + 1
            comment.setLikeCount(comment.getLikeCount() + 1);
        }
        return ResponseEntity.ok().body("좋아요 성공");
    }

    @Transactional
    public ResponseEntity<String> cancelcommentlike(Long postId, Long commentId, User user) {
        Post post = findPost(postId);
        Comment comment = findComment(commentId);

        // 여기서 좋아요가 없는 부분은 throw로 날림 (기록이 아예 없는 경우에 취소하려고 한다면)
        Optional<CommentLike> commentLike = findCommentLike(user, post, comment);

        // 가져온 like의 값이 true 라면
        if (commentLike.get().getLike()) {
            commentLike.get().setLike(false);

            // 해당 댓글의 like - 1
            comment.setLikeCount(comment.getLikeCount() - 1);
        } else {
            throw new IllegalArgumentException("이 게시물에 좋아요가 눌러져 있지 않습니다.");
        }
        return ResponseEntity.ok().body("success");
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }

    private Optional<CommentLike> findCommentLike(User user, Post post, Comment comment) {
        return commentLikeRepository.findByUserAndPostAndComment(user, post, comment);
    }


}
