package com.yapp.pet.domain.accountclub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountClubRepository extends JpaRepository<AccountClub, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from AccountClub ac where ac.account.id = :id")
    int deleteAccountClubsByAccountId(@Param("id") long accountId);

    @Query("select ac from AccountClub ac where ac.club.id = :clubId")
    List<AccountClub> findAccountClubByClubId(@Param("clubId") Long clubId);
}
