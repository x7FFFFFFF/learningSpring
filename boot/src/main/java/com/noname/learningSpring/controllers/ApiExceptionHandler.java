package com.noname.learningSpring.controllers;

import com.noname.learningSpring.entities.web.ErrorItem;
import com.noname.learningSpring.entities.web.Response;
import com.noname.learningSpring.entities.web.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public Response handle(BadCredentialsException e) {
        ErrorItem error = new ErrorItem("401", "Unauthorized");
        return new Response<>(error);
    }
}
