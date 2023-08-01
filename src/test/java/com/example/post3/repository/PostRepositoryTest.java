package com.example.post3.repository;

import com.example.post3.dto.PostRequestDto;
import com.example.post3.entity.Post;
import com.example.post3.entity.UserRoleEnum;
import com.example.post3.query.JPAConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.example.post3.entity.User;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Transactional
@Import(JPAConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("PostRepository: dynamicInsert 테스트")
    void dynamicPostInsertTest() {
        // given
        var newPost = Post.builder().requestDto(new PostRequestDto("title", "contents")).build();

        // when
        postRepository.save(newPost);

        // then
        /* Hibernate:
              insert for
              com.example.post3.entity.Post insert
           into
              post (contents,create_time,modify_time,title)
           values
                (?,?,?,?)
        */
        // 정말 user에 대한 쿼리문은 날라가지 않는다 !
    }

    @Test
    @DisplayName("PostRepository: dynamicUpdate 테스트")
    void dynamicPostUpdateTest() {
        // given
        var newUser = User.builder().username("user").password("password").role(UserRoleEnum.USER).build();
        userRepository.save(newUser);
        var newPost = Post.builder().requestDto(new PostRequestDto("얼굴찌푸리지말아요", "Baby 넌 웃는게 더 예뻐")).user(newUser).build();
        postRepository.save(newPost);

        String newTitle = "";
        String newContent = "";

        // when
        newPost.update(new PostRequestDto(newTitle, newContent));
        var savedPost = postRepository.save(newPost);

        // then
        assertThat(savedPost.getTitle()).isEqualTo(newTitle);
        assertThat(savedPost.getContents()).isEqualTo(newContent);
        /* Hibernate:
               update
               for com.example.post3.entity.Post / update post
            set
               contents=?,
               modify_time=?,
               title=?
            where
               id=?  */
    }

    @Test
    @DisplayName("PostRepository: dynamicInsert & dynamicUpdate 테스트")
    void dynamicPostInsertAndUpdateTest() {
        // given
        var newPost = Post.builder().requestDto(new PostRequestDto("어딜가도니생각뿐야", "Because I'm Alone")).build();
        postRepository.save(newPost);

        var newUser = User.builder().username("user").password("password").role(UserRoleEnum.USER).build();
        userRepository.save(newUser);

        // when
        newPost.setUser(newUser);
        var savedPost = postRepository.save(newPost);

        // then
        assertThat(savedPost.getUser().getUsername()).isEqualTo(newPost.getUser().getUsername());
        /* Hibernate:
            insert for
                com.example.post3.entity.Post / insert
            into
                post (contents,create_time,modify_time,title)
            values
                (?,?,?,?) */

        /* Hibernate:
            update
                for com.example.post3.entity.Post / update post
            set
                modify_time=?,
                username=?
            where
                id=? */
    }


}