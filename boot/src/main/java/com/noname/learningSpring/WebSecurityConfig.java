package com.noname.learningSpring;

import com.noname.learningSpring.repositories.AccountRepository;
import com.noname.learningSpring.security.AccountBuilder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ANONYMOUS_USER_NAME = "anonymous";

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectFactory<AccountBuilder> accountBuilder;

    @Autowired
    private Constants constants;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (constants.csrf) {
            http.csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        } else {
            http.csrf().disable();
        }


        if (!accountRepository.findByName(ANONYMOUS_USER_NAME).isPresent()) {
            accountBuilder.getObject().role(ROLE_ANONYMOUS).userName(WebSecurityConfig.ANONYMOUS_USER_NAME).password("1")
                    .privileges("GET /*/").build();

        }
        http.authorizeRequests().antMatchers("/*.js", "/*.ico", "/*.png", "/*.css",
                constants.entry("login"), constants.entry("logout"), "/admin/login"
        ).permitAll().

                and().authorizeRequests().antMatchers("**").access("@authComponent.auth(authentication, request)")
                .and().anonymous().principal(userDetailsService.loadUserByUsername(ANONYMOUS_USER_NAME));


        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");


        http.authorizeRequests()
                .and().logout().logoutUrl(constants.entry("logout"))
                .logoutSuccessHandler((request, response, authentication) -> {
                    //NOTHING TO DO
                });
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
