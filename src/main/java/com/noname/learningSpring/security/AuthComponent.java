package com.noname.learningSpring.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Component
public class AuthComponent {


    public boolean auth(Authentication authentication, HttpServletRequest request) {
        final String key = String.format("%s %s", request.getMethod(), request.getRequestURI());
        if (check(authentication, key)) return true;
        return false;

      /*  return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(authority->equals(key));*/
    }

    private boolean check(Authentication authentication, String key) {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean auth(Authentication authentication, HttpServletRequest request, String id) {
        final String key = String.format("#%s",  id);
        if (check(authentication, key)) return true;
        return false;
    }
}
