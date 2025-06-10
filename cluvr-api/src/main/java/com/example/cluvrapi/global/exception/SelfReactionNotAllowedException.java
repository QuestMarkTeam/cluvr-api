package com.example.cluvrapi.global.exception;

import lombok.Getter;

import com.example.cluvrapi.global.response.ResponseCode;

public class SelfReactionNotAllowedException extends RuntimeException {
	@Getter
	private final ResponseCode responseCode;

	public SelfReactionNotAllowedException(ResponseCode responseCode) {
		super(responseCode.getDefaultMessage());
		this.responseCode = responseCode;
	}
}
