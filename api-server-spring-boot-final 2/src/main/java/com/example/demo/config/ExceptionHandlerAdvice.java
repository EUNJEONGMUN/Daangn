package com.example.demo.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice //전역에서 발생할 수 있는 예외를 잡아 처리해주는 annotation
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BaseException.class)
    public BaseResponse BaseException(BaseException exception){
        return new BaseResponse<>(exception.getStatus());
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
//        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
//        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
//        boolean isSuccess = false;
//        int code = 0;
//        String message = "";
//        String detail = "";
//
//        //에러가 있다면
//        if(bindingResult.hasErrors()){
//            //DTO에 설정한 meaasge값을 가져온다
//            detail = bindingResult.getFieldError().getDefaultMessage();
//
//            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
//            String bindResultCode = bindingResult.getFieldError().getCode();
//
//            switch (bindResultCode){
//                case "NotBlank":
//                    code = ErrorCode.NOT_BLANK.getCode();
//                    message = ErrorCode.NOT_BLANK.getMessage();
//                    break;
//                case "NotEmpty":
//                    code = ErrorCode.NOT_EMPTY.getCode();
//                    message = ErrorCode.NOT_EMPTY.getMessage();
//                    break;
//                case "Pattern":
//                    code = ErrorCode.PATTERN.getCode();
//                    message = ErrorCode.PATTERN.getMessage();
//                    break;
//                case "Min":
//                    code = ErrorCode.MIN_VALUE.getCode();
//                    message = ErrorCode.MIN_VALUE.getMessage();
//                    break;
//            }
//        }
//
//        return new ErrorResponse(isSuccess, code, message, detail);
//    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        BaseResponse baseResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.BAD_REQUEST);
    }
    private BaseResponse makeErrorResponse(BindingResult bindingResult){
        boolean isSuccess = false;
        int code = 0;
        String message = "";
        String detail = "";

        //에러가 있다면
        if(bindingResult.hasErrors()){
            //DTO에 설정한 meaasge값을 가져온다
            detail = bindingResult.getFieldError().getDefaultMessage();

            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();

            switch (bindResultCode){
                case "NotBlank":
                    code = BaseResponseStatus.NOT_BLANK.getCode();
                    message = BaseResponseStatus.NOT_BLANK.getMessage();
                    break;
                case "NotEmpty":
                    code = BaseResponseStatus.NOT_EMPTY.getCode();
                    message = BaseResponseStatus.NOT_EMPTY.getMessage();
                    break;
                case "Pattern":
                    code = BaseResponseStatus.PATTERN.getCode();
                    message = BaseResponseStatus.PATTERN.getMessage();
                    break;
                case "Min":
                    code = BaseResponseStatus.MIN_VALUE.getCode();
                    message = BaseResponseStatus.MIN_VALUE.getMessage();
                    break;
            }
        }

        return new BaseResponse(isSuccess, code, message, detail);
    }

}
