package com.example.cluvrapi.global.exception;

import com.example.cluvrapi.global.response.ResponseCode;

public class NoPermissionException extends RuntimeException {

	private final ResponseCode code;

	public NoPermissionException(ResponseCode code, String message) {
		super(message);
		this.code = code;
	}

	public ResponseCode getCode() {
		return code;
	}
}
