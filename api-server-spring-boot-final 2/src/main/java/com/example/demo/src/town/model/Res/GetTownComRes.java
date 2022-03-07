package com.example.demo.src.town.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTownComRes {
    private int townPostComId;
    private String comment;
    private String postContent;
    private String uploadTime;
}
