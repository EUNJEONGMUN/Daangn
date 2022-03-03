package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PutTownLikedReq {
    private int comId;
    private int userId;
    private char status;

    public PutTownLikedReq(){

    }
}