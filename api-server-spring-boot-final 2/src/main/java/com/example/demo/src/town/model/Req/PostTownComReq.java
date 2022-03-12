package com.example.demo.src.town.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor

// 동네 생활 댓글 작성 시 필요한 것들
public class PostTownComReq {

    @NotEmpty(message="내용을 입력해주세요.")
    @Min(value=1, message="한 글자 이상 입력해주세요.")
    private String content;

    private int refId;

    public PostTownComReq(){

    }
}
