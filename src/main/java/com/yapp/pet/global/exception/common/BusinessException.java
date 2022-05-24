package com.yapp.pet.global.exception.common;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException{

	private final ExceptionStatus responseStatus;

	public BusinessException(ExceptionStatus responseStatus) {
		super(responseStatus.getMessage());
		this.responseStatus = responseStatus;
	}

	public BusinessException(ExceptionStatus responseStatus, String message) {
		super(message);
		this.responseStatus = responseStatus;
	}

	public HttpStatus getHttpStatus() {
		return responseStatus.getHttpStatus();
	}

	public String getStatus() {
		return String.valueOf(responseStatus.getStatus());
	}

}
