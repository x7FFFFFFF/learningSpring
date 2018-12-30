package com.noname.learningSpring.repositories;

import com.noname.learningSpring.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<Role,Long> {
    Optional<Role> findByName(String name);

    Page<Role> findByNameContainingIgnoreCase(Pageable pageable, String name);
}
