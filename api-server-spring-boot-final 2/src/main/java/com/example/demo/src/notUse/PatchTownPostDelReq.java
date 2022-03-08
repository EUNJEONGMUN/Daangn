package com.example.demo.src.notUse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchTownPostDelReq {
    private int postId;
    private int userId;
    private String status;
}
