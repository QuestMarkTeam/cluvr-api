package com.example.cluvrapi.domain.club.dto.request;

import jakarta.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UpdateClubRequestDto {
	@Size(min = 2, max = 20, message = "스터디 이름은 2~20 글자 사이만 가능합니다.")
	private String name;
	@Size(min = 2, max = 100, message = "소개말은 2자 이상 100자 이하로 작성해주세요.")
	private String greeting;
	@Size(min = 2, max = 255, message = "설명은 2자 이상 255자 이하로 작성해주세요.")
	private String description;
}
