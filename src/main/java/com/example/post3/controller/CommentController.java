package com.example.post3.controller;

import com.example.post3.dto.CommentRequestDto;
import com.example.post3.dto.CommentResponseDto;
import com.example.post3.exception.StatusResponseDto;
import com.example.post3.security.UserDetailsImpl;
import com.example.post3.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping("/post/{id}/comment")
    public CommentResponseDto createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(id, requestDto, userDetails.getUser());
    }

    // 댓글 수정
    @PutMapping("/post/{id}/comment/{commentid}")
    public CommentResponseDto updateComment(@PathVariable Long id, @PathVariable Long commentid, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse res) {
        return commentService.updateCommnet(id, commentid, requestDto, userDetails.getUser(), res);
    }

    @DeleteMapping("/post/{id}/comment/{commentid}")
    public StatusResponseDto deleteComment(@PathVariable Long id, @PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, commentid, userDetails.getUser());
    }
}
