package com.yapp.pet.global.exception.jwt;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class InvalidJwtSignatureException extends BusinessException {

	public InvalidJwtSignatureException() {
		super(ExceptionStatus.INVALID_JWT_SIGNATURE_EXCEPTION);
	}

}
