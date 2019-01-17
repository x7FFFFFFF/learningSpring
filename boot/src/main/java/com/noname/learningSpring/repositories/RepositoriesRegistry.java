package com.noname.learningSpring.repositories;

import com.noname.learningSpring.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class RepositoriesRegistry {
    final Map<Class<?>, CrudRepository> map;

    @Autowired
    public RepositoriesRegistry(AccountRepository accountRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, ProductRepository productRepository) {
        Map<Class<?>, CrudRepository> mapTmp = new HashMap<>();
        mapTmp.put(Account.class, accountRepository);
        mapTmp.put(Role.class, roleRepository);
        mapTmp.put(Privilege.class, privilegeRepository);
        mapTmp.put(Product.class, productRepository);
        map = Collections.unmodifiableMap(mapTmp);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> resolve(EntityReference<T> reference) {
        final CrudRepository crudRepository = map.get(reference.getType());
        if (crudRepository != null) {
            return crudRepository.findById(reference.getId());
        }
        return Optional.empty();
    }


}
