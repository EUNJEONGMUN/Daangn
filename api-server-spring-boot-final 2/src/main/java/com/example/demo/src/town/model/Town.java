package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Town {

    @NotBlank(message="카테고리를 선택해주세요.")
    private int townPostCategoryId;

    @NotBlank(message = "내용을 입력해주세요.")
    @Min(value = 10, message = "내용이 너무 짧습니다.(10글자 이상)")
    private String content;

    private String status;
}
