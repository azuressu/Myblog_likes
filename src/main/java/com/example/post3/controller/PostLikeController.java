package com.example.post3.controller;

import com.example.post3.security.UserDetailsImpl;
import com.example.post3.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/api/post/{postId}/like")
    public ResponseEntity<String> addpostlike(@PathVariable(name = "postId") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("좋아요 시도");
        return postLikeService.addpostlike(postId, userDetails.getUser());
    }

    @PutMapping("/api/post/{postId}/like")
    public ResponseEntity<String> cancelpostlike(@PathVariable(name = "postId") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("좋아요 취소 시도");
        return postLikeService.cancelpostlike(postId, userDetails.getUser());
    }

}
