package com.example.post3.repository;

import com.example.post3.entity.Post;
import com.example.post3.entity.PostLike;
import com.example.post3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);

}
