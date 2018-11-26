package com.noname.learningSpring.security.matchers.id;

public class IdAllMatcher extends AbstractIdMatcher implements IdMatcher {
    private final String pattern = IdMatcherFactory.MATCH_ALL;


    @Override
    public boolean matches(String path) {
        return true;
    }

    @Override
    String getPattern() {
        return pattern;
    }
}
