package com.example.demo.src.memberSample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMemberRes {
    private String jwt;
    private int userIdx;
}
