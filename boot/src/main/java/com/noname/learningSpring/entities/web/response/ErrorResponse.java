package com.noname.learningSpring.entities.web.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorResponse {

    public static ErrorResponse of(ErrorItemResponse... errorItems) {
        List<ErrorItemResponse> res = new ArrayList<>(Arrays.asList(errorItems));
        return new ErrorResponse(res);
    }

    private List<ErrorItemResponse> errorItems;

    public ErrorResponse() {
    }

    public ErrorResponse(List<ErrorItemResponse> errorItems) {
        this.errorItems = errorItems;
    }

    public List<ErrorItemResponse> getErrorItems() {
        return errorItems;
    }

    public void setErrorItems(List<ErrorItemResponse> errorItems) {
        this.errorItems = errorItems;
    }
}
