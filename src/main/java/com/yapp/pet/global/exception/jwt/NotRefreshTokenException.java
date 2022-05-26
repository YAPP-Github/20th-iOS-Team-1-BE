package com.yapp.pet.global.exception.jwt;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class NotRefreshTokenException extends BusinessException {

    public NotRefreshTokenException() {
        super(ExceptionStatus.NOT_REFRESH_TOKEN);
    }

}
