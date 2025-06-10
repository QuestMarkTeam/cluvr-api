package com.example.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class JoinRequestDto {
	@NotNull(message = "사용자 ID는 필수입니다")
	private Long userId;

	public JoinRequestDto(Long userId) {
		this.userId = userId;
	}

	public static JoinRequestDto from(Long userId) {
		return new JoinRequestDto(userId);
	}
}
