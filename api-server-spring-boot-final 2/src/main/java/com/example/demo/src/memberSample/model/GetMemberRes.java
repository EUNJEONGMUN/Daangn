package com.example.demo.src.memberSample.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMemberRes {
    private int userIdx;
    private String userName;
    private String ID;
    private String email;
    private String password;
}
