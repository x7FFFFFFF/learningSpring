package com.noname.learningSpring.security;

import com.noname.learningSpring.entities.Account;
import com.noname.learningSpring.entities.Privilege;
import com.noname.learningSpring.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountBuilder {


    private final LocalSecurityContext context;
    private final List<String> privileges = new ArrayList<>();
    private String role;
    private String userName;
    private String password;

    @Autowired
    public AccountBuilder(LocalSecurityContext context) {
        this.context = context;
    }


    public AccountBuilder privileges(String... privileges) {
        this.privileges.addAll(Arrays.asList(privileges));
        return this;
    }

    public AccountBuilder role(String role) {
        this.role = role;
        return this;
    }

    public AccountBuilder userName(String userName) {
        this.userName = userName;
        return this;
    }

    public AccountBuilder password(String password) {
        this.password = password;
        return this;
    }

    @Transactional
    public Account build() {
        validate();
        final List<Privilege> privilegesList = privileges.stream().map(this::createPrivilegeIfNotFound).collect(Collectors.toList());
        final Role roleObj = createRoleIfNotFound(this.role, privilegesList);
        final Account account = new Account(userName, context.getPasswordEncoder().encode(password),//"$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu",
                true, Collections.singletonList(roleObj));
        return context.getAccountRepository().save(account);
    }

    private void validate() {
        Assert.hasText(userName, "Pattern cannot be null or empty");
        Assert.hasText(password, "Pattern cannot be null or empty");
        Assert.hasText(role, "Pattern cannot be null or empty");
    }


    private Privilege createPrivilegeIfNotFound(String name) {
        Optional<Privilege> privilegeOpt = context.getPrivelegeRep().findByName(name);
        if (!privilegeOpt.isPresent()) {
            Privilege privilege = new Privilege(name);
            context.getPrivelegeRep().save(privilege);
            return privilege;
        }
        return privilegeOpt.get();
    }


    private Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Optional<Role> roleOpt = context.getRoleRepo().findByName(name);
        if (!roleOpt.isPresent()) {
            Role role = new Role(name);
            role.setPrivileges(privileges);
            context.getRoleRepo().save(role);
            return role;
        }
        return roleOpt.get();
    }
}
