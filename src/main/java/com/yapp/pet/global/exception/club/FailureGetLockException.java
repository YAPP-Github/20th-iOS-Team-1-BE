package com.yapp.pet.global.exception.club;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class FailureGetLockException extends BusinessException {

    public FailureGetLockException() {
        super(ExceptionStatus.FAILURE_GET_LOCK_EXCEPTION);
    }

}
