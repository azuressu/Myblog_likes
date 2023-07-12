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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseEntity<String> addpostlike(Long postId, User user) {
        Post post = findPost(postId);

        Optional<PostLike> postLike = findPostLike(user, post);

        // 일단 해당 레포지토리에 PostLike가 있는지 보자
        // 그리고 있으면 ,, 있는거 안에서 true false 인지 판단해서 경우를 다시 나눠주어야 함
        if (postLike.isPresent()) {
            if (postLike.get().getLike()) { // 가져온 like 값이 true 라면
                log.info("좋아요가 이미 눌려있음");
                throw new DuplicateRequestException("좋아요가 이미 눌러져 있습니다");
            } else {
                log.info("좋아요의 false 값을 true로 변경할 것");
                postLike.get().setLike(true);

                // 해당 게시글의 like + 1
                post.setLikeCount(post.getLikeCount() + 1);
                log.info(post.getLikeCount().toString());
            }
        } else { // PostLike가 없다면 ..
            log.info("좋아요 누르기 시도중");
            // 좋아요를 누르겠다고 했으니 새로 레파지토리에 넣어주면 됨
            PostLike postLike1 = new PostLike(user, post);
            postLike1.setLike(true); // true 값으로 넣어서 저장할 것
            postLikeRepository.save(postLike1);

            // 해당 게시글의 like + 1
            post.setLikeCount(post.getLikeCount() + 1);
            log.info(post.getLikeCount().toString());
        }
        return ResponseEntity.ok().body("좋아요 성공");
    }

    @Transactional
    public ResponseEntity<String> cancelpostlike(Long postId, User user) {
        Post post = findPost(postId);

        Optional<PostLike> postLike = findPostLike(user, post);

        // 일단 레파지토리에서 찾는다
        if (postLike.isPresent()) {
            // 그리고 찾은 그 값의 like가 false인지 true인지
            // true면 취소해주면 되고, false면 좋아요가 눌러져 있지 않습니다 해주면 될 듯 ?

            if (postLike.get().getLike()) { // 가져온 like 값이 true 라면
                // false로 수정하기
                postLike.get().setLike(false);

                // 해당 게시글의 like - 1
                post.setLikeCount(post.getLikeCount() - 1);
                postRepository.save(post);
            } else {
                throw new IllegalArgumentException("이 게시글에 좋아요가 눌러져 있지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("이 게시글에 좋아요가 눌러져 있지 않습니다.");
        }
        return ResponseEntity.ok().body("좋아요 취소 성공");
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private Optional<PostLike> findPostLike(User user, Post post) {
        return postLikeRepository.findByUserAndPost(user, post);
    }

}
