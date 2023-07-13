![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=블로그%20백엔드%20서버%20lv4&fontSize=50)


## 📝 블로그 백엔드 서버 만들기 Lv4
✍️ 개인과제 ✍️

## 📍 프로그램 요구사항

1. 회원 가입 API
  - username, password를 Client에서 전달받기
  - username은 최소 4자 이상 10자 이하이며 알파벳 소문자와 숫자로 구성
  - password는 최소 8자 이상 15자 이하이며 알파벳 대소문자와 숫자와 특수문자로 구성
  - DB에 중복된 username이 없다면 회원을 저장하고 Client로 성공했다는 메시지와 상태코드 반환
  - 회원 권한 부여하기 (ADMIN, USER) - ADMIN 회원은 모든 게시글과 댓글 수정 및 삭제 가능

2. 로그인 API
  - username, password를 Client에서 전달받기
  - DB에서 username을 사용해 저장된 회원의 유무를 확인하고, 있다면 password 비교
  - 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용해 토큰을 발급하고, 발급한 토큰을 Header에 추가한 후 성공했다는 메시지와 상태코드 Client에 반환

3. 댓글 작성 API
  - 토큰을 검사하여 유효한 토큰일 경우에만 댓글 작성 가능
  - 선택한 게시글의 DB 저장 유무 확인
  - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환

4. 댓글 수정 API
  - 토큰을 검사하여 유효한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능
  - 선택한 댓글의 DB 저장 유무를 확인
  - 선택한 댓글이 있다면 댓글을 수정하고 수정된 댓글 반환

5. 댓글 삭제 API
  - 토큰을 검사하여 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
  - 선택한 댓글의 DB 저장 유무를 확인
  - 선택한 댓글이 있다면 댓글을 삭제하고 Client로 성공했다는 메시지와 상태코드 반환

6. 전체 게시글 목록을 조회하는 API
  - 제목, 작성자명(username), 작성 내용, 작성 날짜 조회
  - 작성 날짜를 기준으로 내림차순으로 정렬
  - 각 게시글에 등록된 모든 댓글을 게시글과 같이 Client에 반환
  - 댓글은 작성 날짜 기준 내림차순으로 정렬
  - 게시글/댓글에 ‘좋아요’ 개수도 함께 반환

7. 게시글을 작성하는 API
  - Spring Security를 사용해 토큰 검사 및 인증
  - 제목, 작성 내용 저장
  - 저장된 게시글을 Client로 반환

8. 선택한 게시글을 조회하는 API
  - 선택한 게시글의 제목, 작성자명, 작성 날짜, 작성 내용 조회
  - 검색 기능이 아닌, 간단한 게시글 조회로 구현
  - 선택한 게시글에 등록된 모든 댓글을 선택한 게시글과 같이 Client에 반환
  - 댓글은 작성 날짜 기준 내림차순으로 정렬
  - 게시글/댓글에 ‘좋아요’ 개수도 함께 반환

9. 선택한 게시글을 수정하는 API
  - Spring Security를 사용해 토큰 검사 및 인증
  - 제목, 작성 내용을 수정하고 수정된 게시글을 Client로 반환
  - 게시글에 ‘좋아요’ 개수도 함께 반환

10. 선택한 게시글을 삭제하는 API
  - Spring Security를 사용해 토큰 검사 및 인증
  - 선택한 게시글을 삭제하고 Client로 성공했다는 메시지와 상태코드 반환

11. 댓글 작성 API
  - Spring Security를 사용해 토큰 검사 및 인증
  - 선택한 게시글의 DB 저장 유무 확인
  - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환

12. 댓글 수정 API
  - Spring Security를 사용해 토큰 검사 및 인증
  - 선택한 댓글의 DB 저장 유무 확인
  - 선택한 댓글이 있다면 댓글 수정 및 수정된 댓글 반환
  - 댓글에 ‘좋아요’ 개수도 함께 반환

13. 댓글 삭제 API
  - Spring Security를 사용해 토큰 검사 및 인증
  - 선택한 댓글의 DB 저장 유무 확인
  - 선택한 댓글이 있다면 댓글 삭제 및 Client로 성공했다는 메시지와 상태코드 반환

14. 게시글 좋아요 API
  - 사용자는 선택한 게시글에 ‘좋아요’ 할 수 있음
  - 사용자가 이미 ‘좋아요’ 한 게시글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됨
  - 요청이 성공하면 Client로 성공했다는 메시지와 상태코드 반환

12. 댓글 좋아요 API
  - 사용자는 선택한 댓글에 ‘좋아요’를 할 수 있음
  - 사용자가 이미 ‘좋아요’ 한 댓글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됨
  - 요청이 성공하면 Client로 성공했다는 메시지와 상태코드 반환

13. 예외 처리
  - 예외 처리를 AOP를 활용하여 구현
    - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아니라면 “토큰이 유효하지 않습니다.” 라는 에러 메시지와 상태코드 400을 Client로 반환
    - 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우 “작성자만 삭제/수정할 수 있습니다.” 라는 에러 메시지와 상태코드 400을 Client로 반환
    - DB에 이미 존재하는 username으로 회원가입을 요청한 경우 “중복된 username입니다.” 라는 에러 메시지와 상태코드 400을 Client로 반환
    - 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 “회원을 찾을 수 없습니다.” 라는 에러 메시지와 상태코드 400을 Client로 반환
    - 회원가입 시 username과 password의 구성이 알맞지 않으면 에러 메시지와 상태코드 400을 Client에 반환
    
## 🔧 사용한 Tool
<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Spring-6DB33F?&style=flat&logo=spring&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white" style="margin-right: 10px;"/>
  <img src="https://img.shields.io/badge/ApachetTomcat-F8DC75?style=flat&logo=apachetomcat&logoColor=white" style="margin-right: 10px;"/>
  <img src="https://img.shields.io/badge/git-F05032?style=flat&logo=git&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/github-181717?style=flat&logo=github&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Java-007396?&style=flat&logo=Java&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/intellijidea-000000?style=flat&logo=intellijidea&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white" style="margin-right: 10px;">
</div>

## 🪶 API 명세서
<img src="https://github.com/azuressu/Myblog_likes/assets/74248726/546a960e-db24-48e9-9b87-7ad0728604f2"  width="800"/>
<img src="https://github.com/azuressu/Myblog_likes/assets/74248726/d5a139e8-441b-4a53-ac22-a97de9f79f95"  width="800"/>
<img src="https://github.com/azuressu/Myblog_likes/assets/74248726/a8964c49-ab3f-4e36-aff0-4564320da2e3"  width="800"/>
<img src="https://github.com/azuressu/Myblog_likes/assets/74248726/6144ad8e-57c6-4c45-8b07-5d3ffcefe1b9"  width="800"/>
<img src="https://github.com/azuressu/Myblog_likes/assets/74248726/08b13115-9480-434c-8db7-99981da5d743"  width="800"/>
<img src="https://github.com/azuressu/Myblog_likes/assets/74248726/1dcca178-9ffd-4524-b86c-3315e5a8b61c"  width="800"/>

## 📜 테이블 구성
### users (User.java)

|컬럼명|데이터타입|기능|
|---|---|---|
|username (PK)|BIGINT NOT NULL unique|사용자 ID(이름)|
|password|varchar(20) NOT NULL|사용자 비밀번호|
|role|enum NOT NULL|사용자 역할|

### post (Post.java)

|컬럼명|데이터타입|기능|
|---|---|---|
|id (PK)|BIGINT NOT NULL unique|게시글 ID|
|title|varchar(255) NOT NULL|게시글 제목|
|contents|varchar(255) NOT NULL|게시글 내용|
|create_time|varchar(255) NOT NULL|게시글 작성일|
|modify_time|varchar(255) NOT NULL|게시글 수정일|
|like_count|int NOT NULL|게시글 좋아요 수|
|username (FK)|varchar(20) NOT NULL|게시글 작성자|

### comment (Comment.java)

|컬럼명|데이터타입|기능|
|---|---|---|
|id (PK)|BIGINT NOT NULL unique|댓글 ID|
|commentcontents|varchar(255) NOT NULL|댓글 내용|
|create_time|varchar(255) NOT NULL|댓글 작성일|
|modify_time|varchar(255) NOT NULL|댓글 수정일|
|like_count|int NOT NULL|댓글 좋아요 수|
|post_id (FK)|BIGINT NOT NULL|댓글의 게시글|
|username (FK)|BIGINT NOT NULL|댓글 작성자|

### postlikes (PostLike.java)

|컬럼명|데이터타입|기능|
|---|---|---|
|id (PK)|BIGINT NOT NULL unique|게시글 좋아요 ID|
|postlike|bit NOT NULL|게시글 좋아요 여부|
|post_id (FK)|BIGINT NOT NULL|게시글 ID|
|username (FK)|BIGINT NOT NULL|작성자 ID|

### commentlikes (CommentLike.java)

|컬럼명|데이터타입|기능|
|---|---|---|
|id (PK)|BIGINT NOT NULL unique|댓글 좋아요 ID|
|commentlike|bit NOT NULL|댓글 좋아요 여부|
|comment_id (FK)|BIGINT NOT NULL|댓글 ID|
|post_id (FK)|BIGINT NOT NULL|게시글 ID|
|username (FK)|BIGINT NOT NULL|작성자 ID|

## 📃 ERD 설계
### ERD 테이블 관계
<img src="https://github.com/azuressu/Myblog_likes/assets/74248726/b4422fd8-c491-4bba-9a71-c55ec207b9f4"  width="800"/>

### JPA 엔티티 관계
<img src="https://github.com/azuressu/Myblog_likes/assets/74248726/0c327e78-1b80-444c-9f59-5984b20e7721"  width="800"/>

## 🗂️ 파일 구성
1. controller 패키지 – CommentController.java, CommentLikeController.java, PostController.java, PostLikeController.java, UserController.java
2. dto 패키지 – CommentRequestDto.java, CommentResponseDto.java, LoginRequestDto.java, PostRequestDto.java, PostResponseDto.java, SignupRequestDto.java
3. entity 패키지 – Comment.java, MyBlogErrorCode.java, Post.java, Timestapmed.java, User.java, UserRoleEnum.java
4. exception 패키지 – GlobalExceptionHandler.java, StatusResponseDto.java
5. jwt 패키지 – JwtUtil.java
6. repository 패키지 – CommentLikeRepository.java, CommentRepository.java, PostLikeRepository.java, PostRepository,java, UserRepository.java
7. security 패키지 – JwtAuthenticationFilter.java, JwtAuthorizationFilter.java, UserDetailsImpl.java, UserDetailsServiceImpl.java, WebSecurityConfig.java
8. service 패키지 – CommentService.java, CommentLikeService.java, PostLikeService.java, PostService.java, UserService.java 

![Footer](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=footer)
