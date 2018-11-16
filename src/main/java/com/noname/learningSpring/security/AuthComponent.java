package com.noname.learningSpring.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Component
public class AuthComponent {


    public boolean auth(Authentication authentication, HttpServletRequest request) {
        final Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails)principal).isAccessGranted(request);
        }
        return false;
   }



    public boolean auth(Authentication authentication, HttpServletRequest request, String id) {
        final Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails)principal).isAccessGranted(request, id);
        }
        return false;
    }
}
