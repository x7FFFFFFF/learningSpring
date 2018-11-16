package com.noname.learningSpring;

import com.noname.learningSpring.entities.Account;

import com.noname.learningSpring.entities.*;
import com.noname.learningSpring.repositories.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.math.BigDecimal;
import java.util.Random;

@SpringBootApplication
public class LearningSpringApplication {


    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(LearningSpringApplication.class, args);
        initTestData(context);
    }

    @Transactional
    private static void initTestData(ConfigurableApplicationContext context) {
        final AccountRepository accountRepository = context.getBean(AccountRepository.class);
        final PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        final PrivilegeRepository privilegeRepository = context.getBean(PrivilegeRepository.class);
        final RoleRepository roleRepository = context.getBean(RoleRepository.class);

      /*  Privilege getPrivilege = createPrivilegeIfNotFound("GET /admin/accountInfo", privilegeRepository);
        Privilege getPrivilege = createPrivilegeIfNotFound("GET /admin/accountInfo", privilegeRepository);
        Privilege getPrivilege = createPrivilegeIfNotFound("GET /admin/accountInfo", privilegeRepository);*/
        List<Privilege> managerPrivileges = Arrays.asList(
                createPrivilegeIfNotFound("GET /admin/accountInfo", privilegeRepository),
                createPrivilegeIfNotFound("#orderList", privilegeRepository),
                createPrivilegeIfNotFound("#product", privilegeRepository)
        );
        final Role roleManager = createRoleIfNotFound("ROLE_MANAGER", managerPrivileges, roleRepository);


        accountRepository.save(new Account("manager1", passwordEncoder.encode("123"),//"$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu",
                true, "ROLE_MANAGER", Collections.singletonList(roleManager)));  //password: 123

        addProducts(context, 10);
    }

    @Transactional
    private static Privilege createPrivilegeIfNotFound(String name, PrivilegeRepository privilegeRepository) {
        Optional<Privilege> privilegeOpt = privilegeRepository.findByName(name);
        if (!privilegeOpt.isPresent()) {
            Privilege privilege = new Privilege(name);
            privilegeRepository.save(privilege);
            return privilege;
        }
        return privilegeOpt.get();
    }

    @Transactional
    private static Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges, RoleRepository roleRepository) {

        Optional<Role> roleOpt = roleRepository.findByName(name);
        if (!roleOpt.isPresent()) {
            Role role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
            return role;
        }
        return roleOpt.get();
    }

    private static void addProducts(ConfigurableApplicationContext context, int amount) {
        final ProductRepository productRepository = context.getBean(ProductRepository.class);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < amount; i++) {
            Product product = new Product();
            product.setName(product.toString());
            product.setPrice(BigDecimal.valueOf(random.nextInt(10000) + 1));
            product.setCode("code" + product.getPrice().intValue());
            productRepository.save(product);
        }
    }

}

