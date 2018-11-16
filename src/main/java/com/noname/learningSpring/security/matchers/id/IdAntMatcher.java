package com.noname.learningSpring.security.matchers.id;

import org.springframework.util.AntPathMatcher;

public class IdAntMatcher extends AbstractIdMatcher implements IdMatcher {

    private final AntPathMatcher antMatcher;

    private final String pattern;

    public IdAntMatcher(String pattern, boolean caseSensitive) {
        this.pattern = pattern;
        this.antMatcher = createMatcher(caseSensitive);
    }

    @Override
    public boolean matches(String path) {
        return this.antMatcher.match(this.pattern, path);
    }


    private static AntPathMatcher createMatcher(boolean caseSensitive) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setTrimTokens(false);
        matcher.setCaseSensitive(caseSensitive);
        return matcher;
    }

    @Override
    String getPattern() {
        return pattern;
    }
}
