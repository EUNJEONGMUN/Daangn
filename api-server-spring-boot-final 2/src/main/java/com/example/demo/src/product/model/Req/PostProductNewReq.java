package com.example.demo.src.product.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// 중고 거래 글을 쓸 때 필요한 것들
public class PostProductNewReq {
    private int userId;
    private String title;
    private int productPostCategoryId;
    private int productPostLocation;
    private char isProposal;
    private String content;
    private int price;

    public PostProductNewReq(){

    }
}
