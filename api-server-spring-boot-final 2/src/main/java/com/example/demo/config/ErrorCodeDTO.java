package com.example.demo.config;

import lombok.Getter;

public enum ErrorCodeDTO {
    NOT_BLANK(false, 2010, "NOT_NULL"),
    NOT_EMPTY(false, 2011, "NOT_EMPTY"),
    PATTERN(false, 2012, "PATTERN"),
    MIN_VALUE(false, 2013, "MIN");


    @Getter
    private boolean isSuccess;
    @Getter
    private int code;
    @Getter
    private String message;

    ErrorCodeDTO(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
