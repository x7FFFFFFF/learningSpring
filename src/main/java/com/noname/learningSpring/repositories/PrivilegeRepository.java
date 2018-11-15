package com.noname.learningSpring.repositories;

import com.noname.learningSpring.entities.Privilege;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PrivilegeRepository extends CrudRepository<Privilege,Long> {
    Optional<Privilege> findByName(String name);
}
