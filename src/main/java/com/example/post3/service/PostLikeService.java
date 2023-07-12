package com.example.post3.service;

import com.example.post3.dto.LikeRequestDto;
import com.example.post3.entity.Post;
import com.example.post3.entity.PostLike;
import com.example.post3.entity.User;
import com.example.post3.repository.PostLikeRepository;
import com.example.post3.repository.PostRepository;
import com.example.post3.repository.UserRepository;
import com.example.post3.security.UserDetailsImpl;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public ResponseEntity<String> addpostlike(Long postId, UserDetailsImpl userDetails, LikeRequestDto likeRequestDto) {
        Post post = findPost(postId);
        User user = userDetails.getUser();

        log.info(post.getTitle());
        log.info(user.getUsername());

        log.info(String.valueOf(likeRequestDto.isLike()));
        if (likeRequestDto.isLike()) {

            if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
                log.info("좋아요가 이미 눌려있음");
                throw new DuplicateRequestException("좋아요가 이미 눌러져 있습니다");
            } else {
                log.info("좋아요 누르기 시도중");
                PostLike postLike = new PostLike(user, post);
                postLikeRepository.save(postLike);

                // 해당 게시글의 like + 1
                post.setLikeCount(post.getLikeCount() + 1);
                postRepository.save(post);
                log.info(post.getLikeCount().toString());
            }
            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.badRequest().body("error");
        }
    }


    public ResponseEntity<String> deletepostlike(Long postId, UserDetailsImpl userDetails, LikeRequestDto likeRequestDto) {
        Post post = findPost(postId);
        User user = userDetails.getUser();

        if (!likeRequestDto.isLike()) {
            if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
                if (user.equals(postLikeRepository.findByUserAndPost(user, post).get().getUser())) { // 토큰의 사용자와 포스트의 사용자가 같은지
                    PostLike postLike = postLikeRepository.findByUserAndPost(user, post).orElseThrow(()  -> new IllegalArgumentException("이 게시글에 좋아요가 눌러져 있지 않습니다."));
                    postLikeRepository.delete(postLike);

                    // 해당 게시글의 like - 1
                    post.setLikeCount(post.getLikeCount() - 1);
                    postRepository.save(post);
                }
            }
            return ResponseEntity.ok().body("success");
        } else {
//            throw new IllegalArgumentException("이 게시글에 좋아요가 눌러져 있지 않습니다.");
            return ResponseEntity.badRequest().body("error");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }


}
