package com.yapp.pet.global.exception.jwt;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class ExpiredJwtTokenException extends BusinessException {

	public ExpiredJwtTokenException() {
		super(ExceptionStatus.EXPIRED_JWT_TOKEN_EXCEPTION);
	}

}
