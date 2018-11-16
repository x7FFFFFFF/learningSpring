package com.noname.learningSpring.security.matchers.request;

import org.springframework.security.web.util.matcher.RequestMatcher;

public interface RequestMatcherFactory {
    String DELIMITER = " ";

    RequestMatcher get(String pattern);

}
