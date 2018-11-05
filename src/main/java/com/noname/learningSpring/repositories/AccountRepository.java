package com.noname.learningSpring.repositories;

import com.noname.learningSpring.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface  AccountRepository extends CrudRepository<Account,Long> {
    List<Account> findByUserName(String userName);
}
