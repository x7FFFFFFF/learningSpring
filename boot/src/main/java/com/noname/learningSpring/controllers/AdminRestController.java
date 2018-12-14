package com.noname.learningSpring.controllers;



import com.noname.learningSpring.entities.web.request.User;
import com.noname.learningSpring.entities.web.response.Response;
import com.noname.learningSpring.entities.web.response.UserDataResonse;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping(path = "/api/v1/")
public class AdminRestController {

    @Autowired
    private AuthenticationManager authManager;

    @PostMapping("/login")
    public Response<UserDataResonse> login(HttpServletRequest req, @RequestBody User request) {

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword());

        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

       return new Response<>(new UserDataResonse(auth));
    }


}
