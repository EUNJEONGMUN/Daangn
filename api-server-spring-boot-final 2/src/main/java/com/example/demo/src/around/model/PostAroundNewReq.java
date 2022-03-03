package com.example.demo.src.around.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAroundNewReq {
    private int userId;
    private String title;
    private int price;
    private String phoneNumber;
    private String content;
    private char isChat;
    private int aroundPostCategoryId;

    public PostAroundNewReq(){

    }
}
