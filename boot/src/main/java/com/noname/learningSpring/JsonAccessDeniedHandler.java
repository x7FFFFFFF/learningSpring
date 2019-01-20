package com.noname.learningSpring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noname.learningSpring.entities.web.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JsonAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        final Response<Object> error = Response.error(HttpStatus.UNAUTHORIZED);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        String json = new ObjectMapper().writeValueAsString(error.getBody());
        response.getWriter().write(json);
        response.flushBuffer();
    }
}
