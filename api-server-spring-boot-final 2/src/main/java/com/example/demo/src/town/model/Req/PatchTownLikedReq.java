package com.example.demo.src.town.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchTownLikedReq {
    private int postId;
    private int userId;
    private String status;
}
