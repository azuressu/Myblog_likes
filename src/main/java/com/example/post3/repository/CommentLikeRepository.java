package com.example.post3.repository;

import com.example.post3.entity.Comment;
import com.example.post3.entity.CommentLike;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByUserAndPostAndComment(User user, Post post, Comment comment);
    List<CommentLike> findByCommentId(Long id);

}
