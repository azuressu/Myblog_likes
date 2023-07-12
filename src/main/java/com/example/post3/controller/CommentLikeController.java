package com.example.post3.controller;

import com.example.post3.security.UserDetailsImpl;
import com.example.post3.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping()
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/api/post/{postId}/comment/{commentId}/addlike") // ResponseEntity<ApiResponseDto>
    public ResponseEntity<String> addcommentlike(@PathVariable(name = "postId") Long postId,
                                                 @PathVariable(name = "commentId") Long commentId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("좋아요 시도");
        return commentLikeService.addcommentlike(postId, commentId, userDetails.getUser());
    }

    @PostMapping("/api/post/{postId}/comment/{commentId}/cancellike") // ResponseEntity<ApiResponseDto>
    public ResponseEntity<String> cancelcommentlike(@PathVariable(name = "postId") Long postId,
                                                 @PathVariable(name = "commentId") Long commentId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("좋아요 취소 시도");
        return commentLikeService.cancelcommentlike(postId, commentId, userDetails.getUser());
    }


}
