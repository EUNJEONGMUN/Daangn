package com.example.demo.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

@RestControllerAdvice //전역에서 발생할 수 있는 예외를 잡아 처리해주는 annotation
public class ExceptioinController {

//    @ExceptionHandler(value= BaseException.class)
//    public BaseResponse BaseExceptionHandler(BaseException e){
//        System.out.println("오류발생");
//        return new BaseResponse<>(e.getStatus());
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        BindingResult bindingResult = e.getBindingResult();
//
//        StringBuilder builder = new StringBuilder();
//        for (FieldError fieldError : bindingResult.getFieldErrors()) {
//            builder.append("[");
//            builder.append(fieldError.getField());
//            builder.append("](은)는 ");
//            builder.append(fieldError.getDefaultMessage());
//            builder.append(" 입력된 값: [");
//            builder.append(fieldError.getRejectedValue());
//            builder.append("]");
//        }
//
//        return builder.toString();
//    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
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
                    code = ErrorCodeDTO.NOT_BLANK.getCode();
                    message = ErrorCodeDTO.NOT_BLANK.getMessage();
                    break;
                case "NotEmpty":
                    code = ErrorCodeDTO.NOT_EMPTY.getCode();
                    message = ErrorCodeDTO.NOT_EMPTY.getMessage();
                    break;
                case "Pattern":
                    code = ErrorCodeDTO.PATTERN.getCode();
                    message = ErrorCodeDTO.PATTERN.getMessage();
                    break;
                case "Min":
                    code = ErrorCodeDTO.MIN_VALUE.getCode();
                    message = ErrorCodeDTO.MIN_VALUE.getMessage();
                    break;
            }
        }

        return new ErrorResponse(isSuccess, code, message, detail);
    }

}
