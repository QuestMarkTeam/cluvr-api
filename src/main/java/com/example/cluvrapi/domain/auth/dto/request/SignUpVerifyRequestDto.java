package com.example.cluvrapi.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class SignUpVerifyRequestDto {
	/** 가입 시 사용한 이메일 주소 */
	private String email;
	/** 이메일로 전송된 6자리 인증번호 */
	private String code;
}
