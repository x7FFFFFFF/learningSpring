package com.noname.learningSpring.entities.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class Response<T> extends ResponseEntity<T> {
    private final List<ErrorItem> errors;
    private final T result;



    public Response(List<ErrorItem> errors, T result) {
        super(result, HttpStatus.OK);
        this.errors = errors;
        this.result = result;
    }

    public Response(ErrorItem error) {
        this(Collections.singletonList(error), null);
    }


    public Response(List<ErrorItem> errors) {
        this(errors, null);
    }

    public Response(T result) {
        this(Collections.emptyList(), result);
    }

    public List<ErrorItem> getErrors() {
        return errors;
    }

    public T getResult() {
        return result;
    }
}
