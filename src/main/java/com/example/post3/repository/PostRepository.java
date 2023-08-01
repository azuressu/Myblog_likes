package com.example.post3.repository;

import com.example.post3.entity.Post;
import com.example.post3.query.PostRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = Post.class, idClass = Long.class)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuery {

    // 모든 메모들을 수정한 시간을 기준으로 정렬
    List<Post> findAllByOrderByCreateTimeDesc();

}
