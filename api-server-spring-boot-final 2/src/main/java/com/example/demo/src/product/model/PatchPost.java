package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class PatchPost {
    // 수정 가능한 항목들
    @NotEmpty(message="제목을 입력해주세요.")
    @Min(value=5, message="제목을 5글자 이상 입력해주세요.")
    private String title;

    @NotBlank(message="카테고리를 선택해주세요.")
    private int categoryId;

    private int jusoCodeId;

    private String isProposal;

    @NotEmpty(message="내용을 입력해주세요.")
    @Min(value=10, message="내용이 너무 짧습니다.(10글자 이상)")
    private String content;

    private int price;
    private String status;
    private String isHidden;
    private String isExistence;

    public PatchPost(){

    }

}
