package com.yapp.pet.global.exception.Account;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException() {
        super(ExceptionStatus.ACCOUNT_NOT_FOUND_EXCEPTION);
    }
}
