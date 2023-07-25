package com.example.post3.aop;

import com.example.post3.entity.Comment;
import com.example.post3.entity.Post;
import com.example.post3.entity.User;
import com.example.post3.entity.UserRoleEnum;
import com.example.post3.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.sql.ast.tree.from.TableReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionException;

@Slf4j(topic = "RoleCheck")
@Aspect
@Component
public class RoleCheck {

    @Pointcut("execution(* com.example.post3.service.PostService.updatePost(..))")
    private void updatePost(){};

    @Pointcut("execution(* com.example.post3.service.PostService.deletePost(..))")
    private void deletePost(){};

    @Pointcut("execution(* com.example.post3.service.CommentService.updateCommnet())")
    private void updateComment(){};

    @Pointcut("execution(* com.example.post3.service.CommentService.deleteComment())")
    private void deleteComment(){};

    @Around("updatePost() || deletePost()")
    public Object executePostRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 첫 번째 매개변수로 게시글을 받아온다
        Post post = (Post)joinPoint.getArgs()[0];

        // 로그인 한 회원이 없는 경우는 수행시간을 기록하지 않는다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal().getClass() == UserDetailsImpl.class) {
            // 로그인한 회원의 정보
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User loginUser = userDetails.getUser();

            // 게시글의 작성자와 요청자가 같은지 or 권한이 Admin인지 확인한다.
            if ( ! (post.getUser().equals(loginUser) || loginUser.getRole().equals(UserRoleEnum.ADMIN))) {
                log.warn("(AOP) 작성자만 게시글을 수정/삭제할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }
        // 핵심 기능을 수행한다.
        return joinPoint.proceed();
    }

    @Around("updateComment() || deleteComment()")
    public Object executeCommentRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 첫 번째 매개변수로 게시글을 받아온다 (댓글의 id는 두 번째이므로 1을 넣어야 한다)
        Comment comment = (Comment)joinPoint.getArgs()[1];

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal().getClass() == UserDetailsImpl.class) {
            // 로그인한 회원의 정보
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User loginUser = userDetails.getUser();
            
            // 게시글의 작성자와 요청자가 같은지 or 권한이 Admin인지 확인
            if (!(comment.getUser().equals(loginUser) || loginUser.getRole().equals(UserRoleEnum.ADMIN))) {
                log.warn("(AOP) 작성자만 댓글을 수정/삭제할 수 있습니다.");
                throw new RejectedExecutionException();
            }
        }
        // 핵심 기능을 수행한다.
        return joinPoint.proceed();
    }

}
