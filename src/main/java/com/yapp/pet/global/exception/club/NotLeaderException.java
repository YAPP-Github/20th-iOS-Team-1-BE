package com.yapp.pet.global.exception.club;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class NotLeaderException extends BusinessException {

    public NotLeaderException() {
        super(ExceptionStatus.NOT_LEADER_EXCEPTION);
    }
}
