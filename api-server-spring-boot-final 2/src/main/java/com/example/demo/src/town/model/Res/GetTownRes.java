package com.example.demo.src.town.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
public class GetTownRes {


    private String categoryName;
    private int townPostId;
    private String content;
    private String userName;
    private String uploadTime;
    private int likeCount;
    private int comCount;
}
