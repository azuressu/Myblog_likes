package com.example.post3.query;

import com.example.post3.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryQuery {

    Page<Post> searching(PostSearchCond cond, Pageable pageable);

}
