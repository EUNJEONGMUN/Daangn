package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetProductInfo {

    private int productPostId;
    private String title;
    private String jusoName;
    private int price;
    private String state;
    private String uploadTime;
}
