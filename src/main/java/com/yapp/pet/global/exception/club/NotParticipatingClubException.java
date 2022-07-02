package com.yapp.pet.global.exception.club;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class NotParticipatingClubException extends BusinessException {

    public NotParticipatingClubException() {
        super(ExceptionStatus.NOT_PARTICIPATING_CLUB_EXCEPTION);
    }

}
