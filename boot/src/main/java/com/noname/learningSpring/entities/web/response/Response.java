package com.noname.learningSpring.entities.web.response;

import com.noname.learningSpring.entities.web.response.ErrorItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class Response<T> extends ResponseEntity<Map> {
    private static final String ERRORS = "errors";
    private static final String RESULT = "result";
    private static final String TIMESTAMP = "timestamp";



    Response(List<ErrorItemResponse> errors, T result) {
        super(create(errors, result), HttpStatus.OK);
    }



    private static <T> Map create(List<ErrorItemResponse> errors, T result) {
        Map<String, Object> map = new HashMap<>();
        map.put(ERRORS, errors);
        map.put(RESULT, result);
        map.put(TIMESTAMP,  new Date().getTime());
        return map;
    }

    public Response(ErrorItemResponse error){
        this(Collections.singletonList(error), null);
    }


    public Response(T result) {
        this(Collections.emptyList(), result);
    }

    public static <T> Response<T> error(HttpStatus httpStatus) {
        return new Response<>(ErrorItemResponse.of(httpStatus));
    }


}
