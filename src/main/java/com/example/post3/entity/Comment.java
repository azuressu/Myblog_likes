package com.example.post3.entity;

import com.example.post3.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 값 자동 증가
    private Long id;
    @Column(name = "commentcontents", nullable = false)
    private String commentcontents;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ColumnDefault("0")
    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    public Comment(CommentRequestDto commentRequestDto) {
        this.commentcontents = commentRequestDto.getCommentcontents();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.commentcontents = commentRequestDto.getCommentcontents();
    }

}
