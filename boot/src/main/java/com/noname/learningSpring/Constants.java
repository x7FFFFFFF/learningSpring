package com.noname.learningSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    public static final String DELIM = "/";
    public final String apiEntryPoint;
    public final boolean csrf;

    @Autowired
    public Constants(@Value("${app.server.apiEntryPoint}") String apiEntryPoint,
                     @Value("${app.server.csrf}") boolean csrf) {
        this.apiEntryPoint = normalize(apiEntryPoint);
        this.csrf = csrf;
    }

    private String normalize(String apiEntryPoint) {
        if (!apiEntryPoint.endsWith(DELIM)) {
            return apiEntryPoint.trim() + DELIM;
        }
        return apiEntryPoint;
    }

    public String entry(String url) {
        return apiEntryPoint + url;
    }
}
