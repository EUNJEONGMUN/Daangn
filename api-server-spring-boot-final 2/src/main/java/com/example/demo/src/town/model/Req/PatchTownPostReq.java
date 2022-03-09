package com.example.demo.src.town.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchTownPostReq {
    private int townPostId;
    private int userId;
    private int townPostCategoryId;
    private String content;
    private String status;
}
