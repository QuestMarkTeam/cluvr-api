package com.example.cluvrapi.domain.club.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.club.enums.ClubType;

@Getter
public class CreateClubRequestDto {
	@Size(min = 2, max = 20, message = "클럽 이름은 2~20 글자 사이만 가능합니다.")
	private String name;

	@Size(min = 2, max = 100, message = "지정된 카테고리만 등록 가능합니다.")
	private ClubType clubType;

	@Size(min = 2, max = 100, message = "지정된 카테고리만 등록 가능합니다.")
	private CategoryType categoryDetail;

	@Size(min = 0, max = 10000, message = "점수 제한은 0점 이상 10,000점 이하로 가능합니다.")
	private int minScoreRequirement;

	@Size(min = 2, max = 10, message = "인원수는 2명 이상 10명 이하로 가능합니다.")
	private int maxMemberCount;

	@Size(min = 2, max = 100, message = "소개말은 2자 이상 100자 이하로 작성해주세요.")
	private String greeting;

	@Size(min = 2, max = 255, message = "설명은 2자 이상 255자 이하로 작성해주세요.")
	private String description;

	@NotBlank(message = "이미지는 필수값입니다.")
	private String posterUrl;

	@NotBlank(message = "공개여부는 지정된 양식만 가능합니다.")
	private Boolean isPublic;
}
