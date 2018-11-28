package com.noname.learningSpring;

import com.noname.learningSpring.repositories.AccountRepository;
import com.noname.learningSpring.repositories.PrivilegeRepository;
import com.noname.learningSpring.repositories.RoleRepository;
import com.noname.learningSpring.security.AccountBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
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

    public static final String ANONYMOUS = "anonymous";
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PrivilegeRepository privelegeRep;
    @Autowired
    private RoleRepository roleRepo;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
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
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // Requires login with role ROLE_EMPLOYEE or ROLE_MANAGER.
        // If not, it will redirect to /admin/login.
        /*http.authorizeRequests().antMatchers("/admin/orderList", "/admin/order", "/admin/accountInfo")//
                .access("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')");

        // Pages only for MANAGER
        http.authorizeRequests().antMatchers("/admin/product").access("hasRole('ROLE_MANAGER')");*/
        if (!accountRepository.findByUserName(ANONYMOUS).isPresent()) {
            new AccountBuilder(accountRepository, passwordEncoder, privelegeRep, roleRepo).role("ROLE_ANONYMOUS").userName(WebSecurityConfig.ANONYMOUS).password("1")
                    .privileges("GET /*", "POST /login").build();
        }
        http.authorizeRequests().antMatchers("/*.js", "/*.ico", "/*.png", "/*.css", "/login",  "/logout","/h2-console").permitAll().
                and().authorizeRequests().antMatchers("**").access("@authComponent.auth(authentication, request)")
                .and().anonymous().principal(userDetailsService.loadUserByUsername(ANONYMOUS));


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
}
