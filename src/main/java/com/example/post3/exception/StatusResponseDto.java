package com.example.post3.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponseDto {

    // API RESULT를 반환할 DTO
    // 메세지와 상태코드를 반환
    // 예외 처리를 이런 DTO예 담아서 반환해도 되지만 아예 예외를 발생시키는 방법도 있다!
    String message;
    Integer statusCode;
}