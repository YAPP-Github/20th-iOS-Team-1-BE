package com.yapp.pet.global.exception.jwt;

import com.yapp.pet.global.exception.common.BusinessException;
import com.yapp.pet.global.exception.common.ExceptionStatus;

public class AuthorityInfoNotFoundException extends BusinessException {
	public AuthorityInfoNotFoundException() {
		super(ExceptionStatus.AUTHORITY_INFO_NOT_FOUND_EXCEPTION);
	}
}
