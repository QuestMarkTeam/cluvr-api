package com.example.cluvrapi.domain.auth.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenDto {
	private String refreshToken;
}
