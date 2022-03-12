package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TownPostCom {
    @NotEmpty(message="내용을 입력해주세요.")
    @Min(value=1, message="한 글자 이상 입력해주세요.")
    private String content;

    private String status;
}
