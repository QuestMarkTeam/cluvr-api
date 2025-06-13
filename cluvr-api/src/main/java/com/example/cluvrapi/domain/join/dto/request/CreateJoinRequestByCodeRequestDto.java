package com.example.cluvrapi.domain.join.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class CreateJoinRequestByCodeRequestDto {
	@NotBlank
	private String inviteCode;
}
