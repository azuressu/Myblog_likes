package com.example.post3.query;

import com.example.post3.entity.Post;
import com.example.post3.repository.PostRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.post3.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery{

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

    @Override
    public Page<Post> search(PostSearchCond cond, Pageable pageable) {
        var query = jpaQueryFactory.select(post)
                .from(post)
                .where(
                        titleLike(cond.getTitle()),
                        contentsLike(cond.getContents())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        query.orderBy(post.createTime.desc());

        var posts = query.fetch();

        long totalSize = jpaQueryFactory.select(Wildcard.count)
                .from(post)
                .where(
                        titleLike(cond.getTitle()),
                        contentsLike(cond.getContents())
                )
                .fetch().get(0);

        return PageableExecutionUtils.getPage(posts, pageable, () -> totalSize);
    }

    private BooleanExpression titleLike(String title) {
        return post.title.contains(title);
    }

    private BooleanExpression contentsLike(String contents) {
        return post.contents.contains(contents);
    }


}
