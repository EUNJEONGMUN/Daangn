package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class GetProductInfo {

    private String title;
    private int price;
    private String state;
    private String uploadTime;

    public GetProductInfo(String title, int price, String state, String uploadTime){
        this.title = title;
        this.price = price;
        this.state = state;
        this.uploadTime = uploadTime;
    }

}
