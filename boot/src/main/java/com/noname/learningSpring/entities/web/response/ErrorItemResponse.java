package com.noname.learningSpring.entities.web.response;

import org.springframework.http.HttpStatus;

public class ErrorItemResponse {
    private  String code;
    private String message;

    public ErrorItemResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorItemResponse of(HttpStatus httpStatus){
        return new ErrorItemResponse(httpStatus.toString(), httpStatus.getReasonPhrase());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
