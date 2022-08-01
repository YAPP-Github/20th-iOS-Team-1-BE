package com.yapp.pet.global.exception.pet;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class NotFoundPetException extends BusinessException {

    public NotFoundPetException() {
        super(ExceptionStatus.NOT_FOUND_PET_EXCEPTION);
    }

}
