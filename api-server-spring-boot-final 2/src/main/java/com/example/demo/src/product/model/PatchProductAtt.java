package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchProductAtt {
    // 중고 거래 관심 등록 시 받을 변수들
    @NotBlank(message="상태 값을 입력해주세요.")
    private String status;

}
