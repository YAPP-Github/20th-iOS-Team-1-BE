package com.yapp.pet.domain.account.repository;

import com.yapp.pet.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {

    @Modifying
    @Transactional
    @Query("update Account a set a.token = null where a.id = :id")
    void deleteToken(@Param("id") Long id);
}
