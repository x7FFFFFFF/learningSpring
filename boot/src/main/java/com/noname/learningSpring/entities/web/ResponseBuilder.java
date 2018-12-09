package com.noname.learningSpring.entities.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class ResponseBuilder<T> {
    private List<ErrorItem> errors;
    private T result;

    public ResponseBuilder() {
        errors = new ArrayList<>();
    }

    public ResponseBuilder addErrors(List<ErrorItem> errors) {
        this.errors.addAll(errors);
        return this;
    }

    public ResponseBuilder addError(ErrorItem error) {
        this.errors.add(error);
        return this;
    }

    public ResponseBuilder setResult(T result) {
        this.result = result;
        return this;
    }

    public ResponseEntity<Response<T>> create() {
        return new ResponseEntity<>(new Response<>(errors, result), HttpStatus.OK);
    }
}