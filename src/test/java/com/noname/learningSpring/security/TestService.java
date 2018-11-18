package com.noname.learningSpring.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TestService implements MessageService {
    @Override
    @PreAuthorize("@authComponent.auth(authentication, request)")
    public String getMessage() {
        return "OK";
    }
}
