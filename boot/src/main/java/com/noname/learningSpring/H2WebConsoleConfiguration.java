package com.noname.learningSpring;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class H2WebConsoleConfiguration extends WebSecurityConfigurerAdapter {
    private static final String H_2_CONSOLE = "/h2-console/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(H_2_CONSOLE);
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers(H_2_CONSOLE).permitAll();

    }
}
