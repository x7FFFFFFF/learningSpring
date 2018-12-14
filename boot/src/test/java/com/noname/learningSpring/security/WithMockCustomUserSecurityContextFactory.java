package com.noname.learningSpring.security;

import com.noname.learningSpring.entities.Account;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Optional;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final LocalSecurityContext ctx;

    private final ObjectFactory<AccountBuilder> accountBuilder;

    @Autowired
    public WithMockCustomUserSecurityContextFactory(LocalSecurityContext ctx, ObjectFactory<AccountBuilder> accountBuilder) {
        this.ctx = ctx;
        this.accountBuilder = accountBuilder;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        final Optional<Account> account = ctx.getAccountRepository().findByUserName(withMockCustomUser.username());
        final CustomUserDetails principal;
        if (account.isPresent()) {
            principal = ctx.createPrincipal(account.get());
        } else {
            final Account acc = accountBuilder.getObject()
                    .role(withMockCustomUser.role()).userName(withMockCustomUser.username()).password(withMockCustomUser.password())
                    .privileges(withMockCustomUser.priveleges()).build();
            principal = ctx.createPrincipal(acc);
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
