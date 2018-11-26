package com.noname.learningSpring.security.matchers.id;

public interface IdMatcherFactory {
    String MATCH_ALL = "/**";
    String ID_SYMBOL = "#";


    IdMatcher get(String pattern, boolean caseSensitive);

}
