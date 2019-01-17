package com.noname.learningSpring.security;

import com.noname.learningSpring.entities.Account;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final LocalSecurityContext context;

    public MyUserDetailsService(LocalSecurityContext context) {
        this.context = context;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Optional<Account> account = context.getAccountRepository().findByName(username);
        if (!account.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(account.get(), context.getIdMatcherFactory(), context.getRequestMatcherFactory());
    }


}
