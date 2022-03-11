package com.example.demo.src.town.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetTownListRes {


    private String categoryName;
    private int townPostId;
    private String content;
    private String userName;
    private String uploadTime;
    private int likeCount;
    private int comCount;
}
