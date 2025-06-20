package com.example.cluvrapi.global.exception;

import lombok.Getter;

import com.example.cluvrapi.global.response.ResponseCode;

public class AuthenticationException extends RuntimeException {
	@Getter
	private final ResponseCode responseCode;

	public AuthenticationException(ResponseCode responseCode) {
		super(responseCode.getDefaultMessage());
		this.responseCode = responseCode;
	}
}
