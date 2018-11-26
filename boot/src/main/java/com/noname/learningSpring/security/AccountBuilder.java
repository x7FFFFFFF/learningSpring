package com.noname.learningSpring.security;

import com.noname.learningSpring.entities.Account;
import com.noname.learningSpring.entities.Privilege;
import com.noname.learningSpring.entities.Role;
import com.noname.learningSpring.repositories.AccountRepository;
import com.noname.learningSpring.repositories.PrivilegeRepository;
import com.noname.learningSpring.repositories.RoleRepository;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

public class AccountBuilder {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;
    private final List<String> privileges = new ArrayList<>();
    private String role;
    private String userName;
    private String password;

    public AccountBuilder(AccountRepository accountRepository, PasswordEncoder passwordEncoder, PrivilegeRepository privilegeRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
    }

    public AccountBuilder(ConfigurableApplicationContext context) {
        accountRepository = context.getBean(AccountRepository.class);
        passwordEncoder = context.getBean(PasswordEncoder.class);
        privilegeRepository = context.getBean(PrivilegeRepository.class);
        roleRepository = context.getBean(RoleRepository.class);
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
        final Account account = new Account(userName, passwordEncoder.encode(password),//"$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu",
                true, Collections.singletonList(roleObj));
        return accountRepository.save(account);
    }

    private void validate() {
        Assert.hasText(userName, "Pattern cannot be null or empty");
        Assert.hasText(password, "Pattern cannot be null or empty");
        Assert.hasText(role, "Pattern cannot be null or empty");
    }


    private Privilege createPrivilegeIfNotFound(String name) {
        Optional<Privilege> privilegeOpt = privilegeRepository.findByName(name);
        if (!privilegeOpt.isPresent()) {
            Privilege privilege = new Privilege(name);
            privilegeRepository.save(privilege);
            return privilege;
        }
        return privilegeOpt.get();
    }


    private Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Optional<Role> roleOpt = roleRepository.findByName(name);
        if (!roleOpt.isPresent()) {
            Role role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
            return role;
        }
        return roleOpt.get();
    }
}
