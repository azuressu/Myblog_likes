package com.example.post3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "commentlikes")
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name = "commentlike")
    private Boolean like;

    public CommentLike(User user, Post post, Comment commnet) {
        this.user = user;
        this.post = post;
        this.comment = commnet;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

}
