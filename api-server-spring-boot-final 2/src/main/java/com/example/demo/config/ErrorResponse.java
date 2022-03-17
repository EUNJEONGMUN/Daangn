package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    /*
    삭제해도 됨
     */
    private boolean isSuccess;
    private int code;
    private String message;
    private String detail;

    public ErrorResponse(boolean isSuccess, int code, String message, String detail){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.detail = detail;
    }
}
