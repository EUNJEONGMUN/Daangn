package com.example.demo.src.user.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostMessageReq {

    @NotBlank(message = "휴대폰 번호를 입력해주세요")
    @Pattern(regexp = "/^010([0-9]{3,4})([0-9]{4})$/", message = "휴대폰 번호 형식을 확인해주세요.")
    private String phoneNumber;
}
