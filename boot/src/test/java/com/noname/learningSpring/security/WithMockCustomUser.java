package com.noname.learningSpring.security;

import com.noname.learningSpring.WebSecurityConfig;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default WebSecurityConfig.ANONYMOUS_USER_NAME;

    String password();

    String role();

    String[] priveleges();
}
