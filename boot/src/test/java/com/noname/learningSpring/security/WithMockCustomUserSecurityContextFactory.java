package com.noname.learningSpring.security;

import com.noname.learningSpring.WebSecurityConfig;
import com.noname.learningSpring.entities.Account;
import com.noname.learningSpring.repositories.AccountRepository;
import com.noname.learningSpring.repositories.PrivilegeRepository;
import com.noname.learningSpring.repositories.RoleRepository;
import com.noname.learningSpring.security.matchers.id.IdMatcherFactory;
import com.noname.learningSpring.security.matchers.request.RequestMatcherFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private IdMatcherFactory idMatcherFactory;
    @Autowired
    private RequestMatcherFactory requestMatcherFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PrivilegeRepository privelegeRep;
    @Autowired
    private RoleRepository roleRepo;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        final Optional<Account> account = accountRepository.findByUserName(withMockCustomUser.username());
        final CustomUserDetails principal;
        if (account.isPresent()) {
            principal = new CustomUserDetails(account.get(), idMatcherFactory, requestMatcherFactory);
        } else {
            final Account acc = new AccountBuilder(accountRepository, passwordEncoder, privelegeRep, roleRepo)
                    .role(withMockCustomUser.role()).userName(withMockCustomUser.username()).password(withMockCustomUser.password())
                    .privileges(withMockCustomUser.priveleges()).build();
            principal = new CustomUserDetails(acc, idMatcherFactory, requestMatcherFactory);
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
