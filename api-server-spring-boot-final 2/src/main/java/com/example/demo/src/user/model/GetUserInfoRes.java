package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {
    private GetMyInfo getMyInfo;
    private GetMyCount getMyCount;
    private List<GetMyManner> getMyManner;
    private List<GetMyComment> getMyComment;
}
