package com.example.demo.src.memberSample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMemberReq {
    private String UserName;
    private String id;
    private String email;
    private String password;
}
