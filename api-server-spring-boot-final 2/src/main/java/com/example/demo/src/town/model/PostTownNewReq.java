package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
// 동네 생활 글을 쓸 때 필요한 것들.
public class PostTownNewReq {

    private int townPostCategoryId;
    private int townPostLocation;
    private String content;

    public PostTownNewReq(){

    }

}
