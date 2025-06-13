package com.example.cluvrapi.domain.club.dto.response;

import lombok.Getter;

@Getter
public class CreateInviteCodeResponseDto {

	private String inviteCode;

	public CreateInviteCodeResponseDto(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public static CreateInviteCodeResponseDto from(String inviteCode) {
		return new CreateInviteCodeResponseDto(inviteCode);
	}
}
