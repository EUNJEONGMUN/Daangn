package com.example.demo.src.product.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonAutoDetect
public class PatchPostDelReq {
    private int postId;
    private int userId;
    private String isExistence;

    public PatchPostDelReq(){

    }
}
