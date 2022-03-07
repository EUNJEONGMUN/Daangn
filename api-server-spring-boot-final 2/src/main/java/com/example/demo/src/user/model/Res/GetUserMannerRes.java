package com.example.demo.src.user.model.Res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserMannerRes {
    private int userId;
    private String manner;
    private int count;
}
