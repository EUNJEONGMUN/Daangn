package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductRes {
    private int postId;
    private String title;
    private String juso;
    private int price;
    private String firstImg;
    private int attCount;
    private int chatCount;
    private String status;
    //    private Timestamp uploadTime;


}
