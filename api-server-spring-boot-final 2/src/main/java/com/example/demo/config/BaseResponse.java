package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static com.example.demo.config.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }

//    private String detail;
    // DTO ERROR 코드 -> success baseresponse에도 detail이 null값으로 표시됨.
//    public BaseResponse(boolean isSuccess, int code, String message, String detail){
//        this.isSuccess = isSuccess;
//        this.code = code;
//        this.message = message;
//        this.detail = detail;
//    }

    // DTO ERROR
    public BaseResponse(BaseResponseStatus status, String detail){
        this.isSuccess = status.isSuccess();
        this.message = detail;
        this.code = status.getCode();
    }
}

