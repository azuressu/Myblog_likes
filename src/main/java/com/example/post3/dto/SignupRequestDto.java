package com.example.post3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    /* Username과 Password를 Client에서 전달받는다
     * username은 최소 4자 이상, 10자 이하이며 알파벳 소문자 (a-z), 숫자 (0-9)로 구성
     * password는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a-z, A-Z), 숫자(0-9)로 구성
     * DB에 중복된 username이 없다면 회원을 저장하고, Client로 성공했다는 메시지와 상태코드를 반환한다 */

    /*@NotNull : 반드시 값이 있어야 한다.
      @NotEmpty : 반드시 값이 존재하고 길이 혹은 크기가 0보다 커야한다.
      @NotBrank : 반드시 값이 존재하고 공백 문자를 제외한 길이가 0보다 커야 한다.*/

    @NotBlank
//    @Size(min = 4, max = 10)
//    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "알파벳 소문자와 숫자만 입력 가능합니다")
    private String username;

    @NotBlank
//    @Size(min = 8, max = 15, message = "8자에서 15자로 입력해야 합니다")
//    @Pattern(regexp = "^[A-Za-z0-9@$!%*?&]*$", message = "알파벳 대소문자와 숫자, 특수문자만 입력 가능합니다")
    private String password;

    private boolean admin = false;
    private String adminToken = "";

}