package com.noname.learningSpring.security.matchers.request;

import com.noname.learningSpring.security.SecurityRuntimeException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class SimpleRequestFactoryImpl implements RequestMatcherFactory {




    @Override
    public RequestMatcher get(String pattern) {
        if (pattern.contains(DELIMITER)) {
            final String[] split = pattern.split(DELIMITER);
            if (split.length != 2) {
                throw new SecurityRuntimeException("Wrong pattern: '%s'", pattern);
            }
            final String httpMethod = split[0];
            final String url = split[1];
            return new AntPathRequestMatcher(url, httpMethod);
        } else {
            return new AntPathRequestMatcher(pattern);
        }
    }
}
