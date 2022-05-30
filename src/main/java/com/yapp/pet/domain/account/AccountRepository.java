package com.yapp.pet.domain.account;

import com.yapp.pet.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;

public interface AccountRepository extends JpaRepository<Account, Long> {

    default Account findByIdWrapper(Long accountId){
        return findById(accountId).orElseThrow(EntityNotFoundException::new);
    }
}
