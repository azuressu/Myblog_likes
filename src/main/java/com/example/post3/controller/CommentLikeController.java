package com.example.post3.controller;

import com.example.post3.dto.LikeRequestDto;
import com.example.post3.security.UserDetailsImpl;
import com.example.post3.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/api/post/{postId}/{commentId}/like") // ResponseEntity<ApiResponseDto>
    public ResponseEntity<String> addcommentlike(@PathVariable(name = "postId") Long postId,
                                                 @PathVariable(name = "commentId") Long commentId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestBody LikeRequestDto likeRequestDto) {
        log.info("좋아요 시도");
        return commentLikeService.addcommentlike(postId, commentId, userDetails, likeRequestDto);
    }

    @DeleteMapping("/api/post/{postId}/{commentId}/like") // ResponseEntity<ApiResponseDto>
    public ResponseEntity<String> deletecommentlike(@PathVariable(name = "postId") Long postId,
                                                 @PathVariable(name = "commentId") Long commentId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestBody LikeRequestDto likeRequestDto

    ) {
        log.info("좋아요 취소 시도");
        return commentLikeService.deletecommentlike(postId, commentId, userDetails, likeRequestDto);
    }


}
