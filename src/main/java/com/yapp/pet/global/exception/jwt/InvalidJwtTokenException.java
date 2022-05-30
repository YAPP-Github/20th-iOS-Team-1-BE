package com.yapp.pet.global.exception.jwt;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class InvalidJwtTokenException extends BusinessException {

	public InvalidJwtTokenException() {
		super(ExceptionStatus.INVALID_JWT_TOKEN_EXCEPTION);
	}

}
