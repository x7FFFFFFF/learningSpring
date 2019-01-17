package com.noname.learningSpring.controllers;


import com.noname.learningSpring.entities.Role;
import com.noname.learningSpring.entities.web.request.User;
import com.noname.learningSpring.entities.web.response.ErrorItemResponse;
import com.noname.learningSpring.entities.web.response.Response;
import com.noname.learningSpring.entities.web.RoleRest;
import com.noname.learningSpring.entities.web.response.UserDataResonse;
import com.noname.learningSpring.repositories.RepositoriesRegistry;
import com.noname.learningSpring.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping(path = "/api/v1/")
public class AdminRestController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RepositoriesRegistry registry;

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


    @GetMapping("/roles")
    public Response<Page<RoleRest>> getRoles(Pageable pageable, String filter) {
        Page<RoleRest> resp;
        if (filter == null || filter.isEmpty()) {
            resp = roleRepository.findAll(pageable).map(RoleRest::map);
        } else {
            resp = roleRepository.findByNameContainingIgnoreCase(pageable, filter).map(RoleRest::map);
        }
        return new Response<>(resp);
    }

    @PostMapping("/roles")
    Response<RoleRest> createRole(@RequestBody RoleRest roleRest) {
        final Role role = roleRest.convert(registry);
        final Role res = roleRepository.save(role);
        return new Response<>(RoleRest.map(res));
    }

    @PutMapping("/roles/{id}")
    Response<RoleRest> editRole(@RequestBody RoleRest roleRest, @PathVariable long id) {
        final Optional<Role> res = roleRepository.findById(id);
        if (!res.isPresent()) {
            return Response.error(HttpStatus.NOT_FOUND);
        }
        return createRole(roleRest);
    }

    @GetMapping("/roles/{id}")
    Response<RoleRest> getRole(@PathVariable long id) {
        final Optional<Role> res = roleRepository.findById(id);
        return res
                .map(role -> new Response<>(RoleRest.map(role)))
                .orElseGet(() -> Response.error(HttpStatus.NOT_FOUND));
    }

}
