package com.noname.learningSpring.security.matchers.id;

public class IdSubpathMatcher extends AbstractIdMatcher implements IdMatcher {
    private final String subpath;
    private final int length;
    private final boolean caseSensitive;

    public IdSubpathMatcher(String subpath, boolean caseSensitive) {
        assert !subpath.contains("*");
        this.subpath = caseSensitive ? subpath : subpath.toLowerCase();
        this.length = subpath.length();
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(String path) {
        if (!this.caseSensitive) {
            path = path.toLowerCase();
        }
        return path.startsWith(this.subpath)
                && (path.length() == this.length || path.charAt(this.length) == '/');
    }

    @Override
    String getPattern() {
        return subpath;
    }
}
