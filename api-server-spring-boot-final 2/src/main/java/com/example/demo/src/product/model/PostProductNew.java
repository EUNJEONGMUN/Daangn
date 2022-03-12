package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostProductNew {
    @NotEmpty(message="제목을 입력해주세요.")
    @Min(value=5, message="제목을 5글자 이상 입력해주세요.")
    private String title;

    @NotBlank(message="카테고리를 선택해주세요.")
    private int categoryId;

    private String isProposal;

    @NotEmpty(message="내용을 입력해주세요.")
    @Min(value=10, message="내용이 너무 짧습니다.(10글자 이상)")
    private String content;

    private int price;
}
