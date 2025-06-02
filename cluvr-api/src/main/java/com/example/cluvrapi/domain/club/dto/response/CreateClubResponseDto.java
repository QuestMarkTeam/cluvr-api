package com.example.cluvrapi.domain.club.dto.response;

import lombok.Getter;

@Getter
public class CreateClubResponseDto {
	private Long id;

	public CreateClubResponseDto(Long id) {
		this.id = id;
	}

	public static CreateClubResponseDto from(Long userId) {
		return new CreateClubResponseDto(userId);
	}
}
