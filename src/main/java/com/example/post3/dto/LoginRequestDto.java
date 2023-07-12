package com.example.post3.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    /*username, password를 Client에서 전달받음
     * DB에서 username을 사용해 저장된 회원의 유무룰 확인하고 있다면 password 비교하기
     * 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용해 토큰을 발급하고,
     * 발급한 토큰을 Header에 추가하고 성공했다는 메세지, 상태코드와 함께 Client에 반환하기*/

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}