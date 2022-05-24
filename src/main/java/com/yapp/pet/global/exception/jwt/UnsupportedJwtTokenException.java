package com.yapp.pet.global.exception.jwt;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class UnsupportedJwtTokenException extends BusinessException {

	public UnsupportedJwtTokenException() {
		super(ExceptionStatus.UNSUPPORTED_JWT_TOKEN_EXCEPTION);
	}

}
