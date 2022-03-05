package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMyComment {
    private int traderId;
    private String traderName;
    private String traderImg;
    private String uploadTime;
    private String content;
    private String jusoName;
}
