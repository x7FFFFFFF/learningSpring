package com.noname.learningSpring.controllers;

import com.noname.learningSpring.entities.web.response.ErrorItemResponse;
import com.noname.learningSpring.entities.web.response.Response;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public Response handle(BadCredentialsException e) {
        ErrorItemResponse error = new ErrorItemResponse("401", "Unauthorized");
        return new Response<>(error);
    }
}
