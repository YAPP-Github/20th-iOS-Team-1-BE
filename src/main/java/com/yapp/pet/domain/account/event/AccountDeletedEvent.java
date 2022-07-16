package com.yapp.pet.domain.account.event;

import com.yapp.pet.domain.account.entity.Account;
import com.yapp.pet.domain.common.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDeletedEvent {

    private EventType type;

    private Account account;

    public static AccountDeletedEvent of(EventType type, Account account) {
        return new AccountDeletedEvent(type, account);
    }
}
