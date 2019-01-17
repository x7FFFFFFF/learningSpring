package com.noname.learningSpring.entities;

public interface IEntity<T> {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);


    EntityReference<T> toReference();

}
