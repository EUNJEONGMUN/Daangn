package com.example.demo.src.product.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostProductAttReq {

    @NotBlank(message="상태 값을 입력해주세요.")
    private String status;

}
