package com.noname.learningSpring;

import com.noname.learningSpring.repositories.AccountRepository;
import com.noname.learningSpring.security.AccountBuilder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ANONYMOUS_USER_NAME = "anonymous";
    public static final String H_2_CONSOLE = "/h2-console/**";
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
                    .ignoringAntMatchers(H_2_CONSOLE)
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        } else {
            http.csrf().disable();
        }

        http.antMatcher(H_2_CONSOLE).headers().frameOptions().disable();

        // Requires login with role ROLE_EMPLOYEE or ROLE_MANAGER.
        // If not, it will redirect to /admin/login.
        /*http.authorizeRequests().antMatchers("/admin/orderList", "/admin/order", "/admin/accountInfo")//
                .access("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')");

        // Pages only for MANAGER
        http.authorizeRequests().antMatchers("/admin/product").access("hasRole('ROLE_MANAGER')");*/
        if (!accountRepository.findByUserName(ANONYMOUS_USER_NAME).isPresent()) {
            accountBuilder.getObject().role(ROLE_ANONYMOUS).userName(WebSecurityConfig.ANONYMOUS_USER_NAME).password("1")
                    .privileges("GET /*/", "GET /h2-console/**", "POST /h2-console/**").build();

        }
        http.authorizeRequests().antMatchers("/*.js", "/*.ico", "/*.png", "/*.css",
                "/login", "/logout", String.format("%slogin", constants.apiEntryPoint),
                "/h2-console**").permitAll().
                //and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).

                        and().authorizeRequests().antMatchers("**").access("@authComponent.auth(authentication, request)")
                .and().anonymous().principal(userDetailsService.loadUserByUsername(ANONYMOUS_USER_NAME));


        // When user login, role XX.
        // But access to the page requires the YY role,
        // An AccessDeniedException will be thrown.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // Configuration for Login Form.
        http.authorizeRequests()
                /*   .and()
                   .formLogin()//
                   .loginProcessingUrl("/j_spring_security_check") // Submit URL
                   .loginPage("/admin/login")//
                   .defaultSuccessUrl("/admin/accountInfo")//
                   .failureUrl("/admin/login?error=true")//
                   .usernameParameter("userName")//
                   .passwordParameter("password")*/

                // Configuration for the Logout page.
                // (After logout, go to home page)
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


/*    @Bean
    public ViewResolver adminViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".html");
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return resolver;
    }*/
}
