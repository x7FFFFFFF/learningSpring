package com.noname.learningSpring.controllers;

import com.noname.learningSpring.entities.ErrorItem;
import com.noname.learningSpring.entities.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handle(BadCredentialsException e) {
        ErrorItem error = new ErrorItem("401", "Unauthorized");
        return new ResponseEntity<>(ErrorResponse.of(error), HttpStatus.OK); //TODO: HttpStatus.BAD_REQUEST
    }

/*    @SuppressWarnings("rawtypes")
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorItem> handle(ResourceNotFoundException e) {
        ErrorItem error = new ErrorItem();
        error.setMessage(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }*/

}
