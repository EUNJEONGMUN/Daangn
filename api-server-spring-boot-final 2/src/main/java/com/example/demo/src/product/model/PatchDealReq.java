package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchDealReq {
    private int postId;
    private int userId;
    private String status;

    public PatchDealReq(){

    }



}
