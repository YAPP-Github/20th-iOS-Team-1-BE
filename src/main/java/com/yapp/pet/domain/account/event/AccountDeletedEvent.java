package com.yapp.pet.domain.account.event;

import com.yapp.pet.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDeletedEvent {

    private Account account;

    public static AccountDeletedEvent from(Account account) {
        return new AccountDeletedEvent(account);
    }
}
