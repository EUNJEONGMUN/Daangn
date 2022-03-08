package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductPost {
    // 수정 가능한 항목들
    private String title;
    private int categoryId;
    private int jusoCodeId;
    private String isProposal;
    private String content;
    private int price;
    private String status;
    private String isHidden;
    private String isExistence;

    public ProductPost(){

    }

}
