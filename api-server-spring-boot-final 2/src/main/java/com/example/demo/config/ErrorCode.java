package com.example.demo.config;

import lombok.Getter;

public enum ErrorCode {
    /**
     * 삭제해도 됨.
     */
    NOT_BLANK(false, 2012, "NOT_NULL"),
    NOT_EMPTY(false, 2013, "NOT_EMPTY"),
    PATTERN(false, 2014, "PATTERN"),
    MIN_VALUE(false, 2015, "MIN");


    @Getter
    private boolean isSuccess;
    @Getter
    private int code;
    @Getter
    private String message;

    ErrorCode(boolean isSuccess, int code, String message){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
