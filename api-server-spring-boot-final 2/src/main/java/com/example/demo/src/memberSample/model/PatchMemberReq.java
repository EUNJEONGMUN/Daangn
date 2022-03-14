package com.example.demo.src.memberSample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchMemberReq {
    private int userIdx;
    private String userName;
}
