package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostNewsReq {

    private int storeId;
    private String title;
    private String content;

    public PostNewsReq(){

    }
}


