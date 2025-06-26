package com.example.cluvrapi.domain.til.dto.reqeust;

import jakarta.validation.constraints.Size;

import lombok.Getter;

@Getter
public class CreateTilRequestDto {
	@Size(min = 2, max = 20, message = "제목은 2자 이상 20자 이하로 작성해주세요.")
	private String title;
	@Size(min = 2, max = 1000, message = "TIL 내용은 2자 이상 1,000자 이하로 작성해주세요.")
	private String content;
}
