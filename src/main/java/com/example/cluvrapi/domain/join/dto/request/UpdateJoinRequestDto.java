package com.example.cluvrapi.domain.join.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

/**
 * Join Request 에 필요한 정보
 */

@Getter
public class UpdateJoinRequestDto {
	/**
	 * 가입 신청 답변
	 */
	@NotBlank
	private String answer;
}
