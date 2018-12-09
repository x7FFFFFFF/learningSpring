package com.noname.learningSpring.entities.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorResponse {

    public  static  ErrorResponse of(ErrorItem... errorItems){
        List<ErrorItem> res = new ArrayList<>(Arrays.asList(errorItems));
        return new ErrorResponse(res);
    }

    private  List<ErrorItem>  errorItems;

    public ErrorResponse() {
    }

    public ErrorResponse(List<ErrorItem> errorItems) {
        this.errorItems = errorItems;
    }

    public List<ErrorItem> getErrorItems() {
        return errorItems;
    }

    public void setErrorItems(List<ErrorItem> errorItems) {
        this.errorItems = errorItems;
    }
}
