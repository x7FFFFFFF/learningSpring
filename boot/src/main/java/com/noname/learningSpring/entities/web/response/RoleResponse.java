package com.noname.learningSpring.entities.web.response;

import com.noname.learningSpring.entities.Privilege;
import com.noname.learningSpring.entities.Role;

public class RoleResponse {

    private final String name;
    private final String parent;
    private final String[] privileges;

    RoleResponse(Role role) {
        this.name = role.getName();
        final Role parent = role.getParent();
        this.parent = (parent != null) ? parent.getName() : null;
        this.privileges = role.getPrivileges().stream().map(Privilege::getName).toArray(String[]::new);
    }
    public static RoleResponse map(Role role) {
        return new RoleResponse(role);
    }


    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public String[] getPrivileges() {
        return privileges;
    }
}
