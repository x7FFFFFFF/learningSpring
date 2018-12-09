package com.noname.learningSpring.controllers;


import com.noname.learningSpring.entities.web.Response;
import com.noname.learningSpring.entities.web.ResponseBuilder;
import com.noname.learningSpring.entities.web.UserDataResonse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping(path = "/api/v1/")
public class AdminRestController {

    @Autowired
    private AuthenticationManager authManager;

    @PostMapping("/login")
    public Response<UserDataResonse> login(HttpServletRequest req, @RequestBody Map<String, String> request) {
        final String userName = request.get("userName");
        final String password = request.get("password");

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(userName, password);

        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

       return new Response<>(new UserDataResonse(auth));
    }


}
