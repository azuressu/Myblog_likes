package com.example.post3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@EqualsAndHashCode
public class User {

    // 중복 안됨
    @Id
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User(String username, String password , UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /* @EqualsAndHashCode
    * 객체의 동등성을 비교하기 위해서 사용한다
    * 자바에서 객체의 동등성을 판단하기 위해서는 equals()와 hashCode() 메서드를 오버라이드 해야 하는데, 번거로워 버그가 발생할 수 있는 부분이다
    * 이 에너테이션을 사용해 자동으로 메서드들을 생성해주어 편의성과 안정성이 높아진다
    * equals() :  두 객체의 내용이 같은지, 동등성(equality) 를 비교하는 연산자
    * hashCode() : 두 객체가 같은 객체인지, 동일성(identity) 를 비교하는 연산자 */

}
