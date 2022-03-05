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
    private int productPostCategoryId;
    private int productPostLocation;
    private String isProposal;
    private String content;
    private int price;
    private String state;

    public ProductPost(){

    }

}
