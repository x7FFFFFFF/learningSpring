package com.noname.learningSpring.repositories;

import com.noname.learningSpring.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface  AccountRepository extends CrudRepository<Account,Long> {
    Optional<Account> findByUserName(String userName);
}
