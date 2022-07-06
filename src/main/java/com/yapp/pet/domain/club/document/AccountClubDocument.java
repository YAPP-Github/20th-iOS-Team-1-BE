package com.yapp.pet.domain.club.document;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountClubDocument {

    private Long clubId;

    private Long accountId;

    private AccountClubDocument(Long clubId, Long accountId) {
        this.clubId = clubId;
        this.accountId = accountId;
    }

    public static AccountClubDocument of(Long cludId, Long accountId) {
        return new AccountClubDocument(cludId, accountId);
    }
}