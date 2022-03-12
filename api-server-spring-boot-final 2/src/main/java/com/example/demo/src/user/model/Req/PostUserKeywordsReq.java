package com.example.demo.src.user.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUserKeywordsReq {

    @Size(min=1)
    @NotBlank(message="한 글자 이상의 키워드를 입력해주세요.")
    private String keyword;

}
