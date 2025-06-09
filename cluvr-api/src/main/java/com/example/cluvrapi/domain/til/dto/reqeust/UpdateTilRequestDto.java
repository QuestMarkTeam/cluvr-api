package com.example.cluvrapi.domain.til.dto.reqeust;

import jakarta.validation.constraints.Size;

import lombok.Getter;

@Getter
public class UpdateTilRequestDto {
	@Size(min = 2, max = 20, message = "제목은 2자 이상 20자 이하로 작성해주세요.")
	private String title;
	@Size(min = 2, max = 255, message = "설명은 2자 이상 255자 이하로 작성해주세요.")
	private String content;
}
