package com.noname.learningSpring.entities.web;

import com.noname.learningSpring.security.CustomUserDetails;
import org.springframework.security.core.Authentication;

import java.util.Set;

public class UserDataResonse {
    private final String name;

    private final Set<String> roles;

    public UserDataResonse(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        this.name = userDetails.getUsername();
        this.roles = userDetails.getRoles();
    }

    public String getName() {
        return name;
    }

    public Set<String> getRoles() {
        return roles;
    }


}
