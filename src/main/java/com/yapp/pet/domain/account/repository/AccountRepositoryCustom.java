package com.yapp.pet.domain.account.repository;

import com.yapp.pet.domain.account.entity.Account;

import java.util.Optional;

public interface AccountRepositoryCustom {

    Optional<Account> findByNickname(String nickname);

    Account findAccount(String uniqueIdBySocial);
}
