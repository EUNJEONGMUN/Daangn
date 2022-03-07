package com.example.demo.src.product.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    private String firstImg;
    private int productPostId;
    private String title;
    private String jusoName;
    private int price;
    private String state;
    private int chatCount;
    private int attCount;
    private String uploadTime;

}
