package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchTownComLikedReq {
    private int postId;
    private int comId;
    private int userId;
    private String status;

    public PatchTownComLikedReq(){

    }
}
