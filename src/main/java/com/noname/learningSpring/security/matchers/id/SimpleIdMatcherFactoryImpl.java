package com.noname.learningSpring.security.matchers.id;

import com.noname.learningSpring.security.SecurityRuntimeException;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SimpleIdMatcherFactoryImpl implements IdMatcherFactory {

    @Override
    public IdMatcher get(String pattern, boolean caseSensitive) {
        if (!pattern.contains(ID_SYMBOL)) {
            throw new SecurityRuntimeException("Wrong pattern: %s", pattern);
        }
        Assert.hasText(pattern, "Pattern cannot be null or empty");
        if (pattern.equals(MATCH_ALL) || pattern.equals("**")) {
            return new IdAllMatcher();
        } else {            // If the pattern ends with {@code /**} and has no other wildcards or path
            // variables, then optimize to a sub-path match
            if (pattern.endsWith(MATCH_ALL)
                    && (pattern.indexOf('?') == -1 && pattern.indexOf('{') == -1
                    && pattern.indexOf('}') == -1)
                    && pattern.indexOf("*") == pattern.length() - 2) {
                return new IdSubpathMatcher(
                        pattern.substring(0, pattern.length() - 3), caseSensitive);
            } else {
                return new IdAntMatcher(pattern, caseSensitive);
            }
        }
    }
}
