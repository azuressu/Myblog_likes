package com.example.post3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
// JPA 엔티티들이 이 추상 클래스를 상속받을 경우, 추상 클래스의 멤버 변수를 컬럼으로 인식함(자바에서의 상속과 동일한 개념)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {

    @CreatedDate
    @Column(updatable = false)        // 이후에는 값이 수정되지 않음
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifyTime;

}