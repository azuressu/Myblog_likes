package com.example.post3.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String contents;

    @Builder
    public PostRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
