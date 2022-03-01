package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

// 동네 생활 댓글 작성 시 필요한 것들
public class PostTownComReq {
    private int townPostId;
    private int userId;
    private String content;
    private int refId;

    public PostTownComReq(){

    }
}
