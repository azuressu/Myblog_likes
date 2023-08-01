package com.example.post3.query;

import lombok.Getter;

@Getter
public class PostSearchCond {

    // 게시글에 대한 제목과 내용으로 검색하기
    private String title;
    private String contents;
}
