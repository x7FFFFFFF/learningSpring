package com.noname.learningSpring.security;

public class SecurityRuntimeException extends RuntimeException {
    public SecurityRuntimeException(String msg, Object... params) {
        super(String.format(msg, params));
    }
}
