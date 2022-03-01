package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutTownComLikedReq {
    private int postId;
    private int userId;
    private char status;

    public PutTownComLikedReq(){

    }
}
