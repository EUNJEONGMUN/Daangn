package com.example.demo.src.town.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchTownPostComReq {
    private int postId;
    private int comId;
    private int userId;
    private String content;
    private String status;
}
