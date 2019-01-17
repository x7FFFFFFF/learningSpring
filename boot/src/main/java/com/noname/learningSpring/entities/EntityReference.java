package com.noname.learningSpring.entities;

import com.noname.learningSpring.repositories.RepositoriesRegistry;

import java.util.Optional;

public class EntityReference<T> {
    Long id;
    String name;
    Class<T> type;



    public EntityReference(Long id, String name, Class<T> type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public static <T> EntityReference<T> of(Long id, String name, Class<T> type) {
        return new EntityReference<T>(id, name, type);
    }

    public EntityReference() {
    }


    public Optional<T> resolve(RepositoriesRegistry registry) {
       return registry.resolve(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
