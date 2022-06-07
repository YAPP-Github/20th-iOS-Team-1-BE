package com.yapp.pet.global.exception.jwt;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class NotFoundTokenException extends BusinessException {

    public NotFoundTokenException() {
        super(ExceptionStatus.NOT_FOUND_TOKEN_EXCEPTION);
    }

}
