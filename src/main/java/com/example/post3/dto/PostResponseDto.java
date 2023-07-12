package com.example.post3.dto;

import com.example.post3.entity.Comment;
import com.example.post3.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.likecount = post.getLikeCount();
        this.contents = post.getContents();
        this.createTime = post.getCreateTime();
        this.modifyTime = post.getModifyTime();

        // post에 저장된 commentList Comment들을 하나씩 저장해준다
        // 날짜 거꾸로를 출력하고 싶어서, 거꾸로 리스트에 담아준다
        if (post.getCommentList().size() > 0) {
            for (Comment comment : post.getCommentList()) {
                this.commentList.add(new CommentResponseDto(comment));
            }
        }
    }
}