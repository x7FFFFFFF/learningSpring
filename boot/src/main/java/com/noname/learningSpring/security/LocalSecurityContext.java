package com.noname.learningSpring.security;

import com.noname.learningSpring.entities.Account;
import com.noname.learningSpring.repositories.AccountRepository;
import com.noname.learningSpring.repositories.PrivilegeRepository;
import com.noname.learningSpring.repositories.RoleRepository;
import com.noname.learningSpring.security.matchers.id.IdMatcherFactory;
import com.noname.learningSpring.security.matchers.request.RequestMatcherFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LocalSecurityContext {


    private final AccountRepository accountRepository;

    private final IdMatcherFactory idMatcherFactory;

    private final RequestMatcherFactory requestMatcherFactory;

    private final PasswordEncoder passwordEncoder;

    private final PrivilegeRepository privelegeRep;

    private final RoleRepository roleRepo;

    @Autowired
    public LocalSecurityContext(AccountRepository accountRepository, IdMatcherFactory idMatcherFactory, RequestMatcherFactory requestMatcherFactory, PasswordEncoder passwordEncoder, PrivilegeRepository privelegeRep, RoleRepository roleRepo) {
        this.accountRepository = accountRepository;
        this.idMatcherFactory = idMatcherFactory;
        this.requestMatcherFactory = requestMatcherFactory;
        this.passwordEncoder = passwordEncoder;
        this.privelegeRep = privelegeRep;
        this.roleRepo = roleRepo;
    }

    public CustomUserDetails createPrincipal(Account account){
        return new CustomUserDetails(account, getIdMatcherFactory(), getRequestMatcherFactory());
    }


    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public IdMatcherFactory getIdMatcherFactory() {
        return idMatcherFactory;
    }

    public RequestMatcherFactory getRequestMatcherFactory() {
        return requestMatcherFactory;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public PrivilegeRepository getPrivelegeRep() {
        return privelegeRep;
    }

    public RoleRepository getRoleRepo() {
        return roleRepo;
    }
}
