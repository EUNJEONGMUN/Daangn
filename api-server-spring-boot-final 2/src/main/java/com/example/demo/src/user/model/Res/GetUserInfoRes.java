package com.example.demo.src.user.model.Res;

import com.example.demo.src.user.model.GetMyComment;
import com.example.demo.src.user.model.GetMyCount;
import com.example.demo.src.user.model.GetMyInfo;
import com.example.demo.src.user.model.GetMyManner;
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
