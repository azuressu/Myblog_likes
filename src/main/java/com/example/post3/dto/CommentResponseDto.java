package com.example.post3.dto;

import com.example.post3.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String username;
    private String commentcontents;
    private Integer likecount;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUser().getUsername();
        this.commentcontents = comment.getCommentcontents();
        this.likecount = comment.getLikeCount();
        this.createTime = comment.getCreateTime();
        this.modifyTime = comment.getModifyTime();
    }
}
