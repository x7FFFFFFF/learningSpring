package com.noname.learningSpring.repositories;

import com.noname.learningSpring.entities.Account;
import org.springframework.data.repository.CrudRepository;


public interface  AccountRepository extends CrudRepository<Account,Long> {
    Account findByUserName(String userName);
}
