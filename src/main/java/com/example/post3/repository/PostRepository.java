package com.example.post3.repository;

import com.example.post3.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 모든 메모들을 수정한 시간을 기준으로 정렬
    List<Post> findAllByOrderByCreateTimeDesc();

}
