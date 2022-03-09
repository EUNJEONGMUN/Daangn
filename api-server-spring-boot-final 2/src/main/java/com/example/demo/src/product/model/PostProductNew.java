package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostProductNew {
//    private int userId;
    private String title;
    private int categoryId;
//    private int jusoCodeId;
    private String isProposal;
    private String content;
    private int price;
}
