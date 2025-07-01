package com.example.cluvrapi.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SocialLoginRequestDto {
	@NotBlank
	private String code;

	@JsonCreator
	public SocialLoginRequestDto(@JsonProperty("code") String code) {
		this.code = code;
	}


}
