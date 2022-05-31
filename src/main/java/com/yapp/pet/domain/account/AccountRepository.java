package com.yapp.pet.domain.account;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.global.exception.Account.AccountNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    default Account findByIdWrapper(Long accountId){
        return findById(accountId).orElseThrow(AccountNotFoundException::new);
    }
}
