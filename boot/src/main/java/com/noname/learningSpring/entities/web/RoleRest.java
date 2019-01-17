package com.noname.learningSpring.entities.web;

import com.noname.learningSpring.entities.EntityReference;
import com.noname.learningSpring.entities.Privilege;
import com.noname.learningSpring.entities.Role;
import com.noname.learningSpring.repositories.RepositoriesRegistry;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleRest {

    private final Long id;
    private final String name;
    private final EntityReference<Role> parent;

    private final List<EntityReference<Privilege>> privileges;

    RoleRest(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        final Role parent = role.getParent();
        if (parent != null) {
            this.parent = parent.toReference();
        } else {
            this.parent = null;
        }

        privileges = role.getPrivileges().stream()
                .map(Privilege::toReference).collect(Collectors.toList());


    }

    public static RoleRest map(Role role) {
        return new RoleRest(role);
    }

    public Role convert(RepositoriesRegistry registry) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        if (parent != null) {
            registry.resolve(parent).ifPresent(role::setParent);
        }
        role.setPrivileges(privileges.stream()
                .map(registry::resolve)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));

        return role;
    }


    public String getName() {
        return name;
    }

    public EntityReference<Role> getParent() {
        return parent;
    }

    public Long getId() {
        return id;
    }

    public List<EntityReference<Privilege>> getPrivileges() {
        return privileges;
    }
}
