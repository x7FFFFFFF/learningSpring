package com.noname.learningSpring.security.matchers.id;

import java.util.Objects;

public abstract class AbstractIdMatcher {

    abstract String getPattern();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdAllMatcher that = (IdAllMatcher) o;
        return Objects.equals(getPattern(), that.getPattern());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPattern());
    }
}
