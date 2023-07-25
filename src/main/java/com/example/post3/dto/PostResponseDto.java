package com.example.post3.dto;

import com.example.post3.entity.Comment;
import com.example.post3.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class PostResponseDto {

    private Long id;
    private String username;
    private String contents;
    private String title;
    private Integer likecount;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private List<CommentResponseDto> commentList;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.likecount = post.getLikeCount();
        this.contents = post.getContents();
        this.createTime = post.getCreateTime();
        this.modifyTime = post.getModifyTime();
        // 댓글 내림차순 정렬
        this.commentList = post.getCommentList().stream()
                        .sorted(Comparator.comparing(Comment::getCreateTime))
                        .map(CommentResponseDto::new).toList();
    }
}