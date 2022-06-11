package com.yapp.pet.domain.account.repository;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.exception.account.AccountNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

}
