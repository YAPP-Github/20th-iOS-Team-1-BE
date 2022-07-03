package com.yapp.pet.global.exception.club;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class NotHaveAnyPetException extends BusinessException {

    public NotHaveAnyPetException() {
        super(ExceptionStatus.NOT_HAVING_ANY_PET_EXCEPTION);
    }
}
