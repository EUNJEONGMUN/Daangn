package com.example.demo.src.user.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserAttentionRes {
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
