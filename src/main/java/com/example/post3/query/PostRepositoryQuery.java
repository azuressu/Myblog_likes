package com.example.post3.query;

import com.example.post3.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.post3.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryQuery {

    // QueryDSL 라이브러리를 사용해 JPA 쿼리를 생성하고 실행하기 위한 클래스
    private final JPAQueryFactory jpaQueryFactory;

    public List<Post> search(PostSearchCond cond) {
        var query = jpaQueryFactory.select(post)
                .from(post)
                .where(
                        titleLike(cond.getTitle()),
                        contentsLike(cond.getContents())
                );

        var posts = query.fetch();
        return posts;
    }

    private BooleanExpression titleLike(String title) {
        return post.title.contains(title);
    }

    private BooleanExpression contentsLike(String contents) {
        return post.contents.contains(contents);
    }

}
