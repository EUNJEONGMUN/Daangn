package com.example.demo.src.user.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchMyInfoReq {
    private int userId;
    private String userImg;
    private String name;
}
