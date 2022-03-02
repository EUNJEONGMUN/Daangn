package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutProductAttReq {
    private int userId;
    private int postId;
    private char status;

    public PutProductAttReq(){

    }

}
