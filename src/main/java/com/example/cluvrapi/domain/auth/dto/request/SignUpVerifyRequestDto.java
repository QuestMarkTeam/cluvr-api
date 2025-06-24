package com.example.cluvrapi.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class SignUpVerifyRequestDto {
	/** 가입 시 사용한 이메일 주소 */
	@NotBlank(message = "이메일은 필수입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;
	/** 이메일로 전송된 6자리 인증번호 */
	@NotBlank(message = "인증 코드는 필수입니다.")
	@Pattern(regexp = "^[0-9]{6}$", message = "인증 코드는 6자리 숫자여야 합니다.")
	private String code;
}
