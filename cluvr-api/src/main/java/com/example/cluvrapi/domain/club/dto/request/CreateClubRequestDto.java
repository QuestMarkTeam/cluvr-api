package com.example.cluvrapi.domain.club.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;

import com.example.cluvrapi.domain.category.enums.CategoryType;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.enums.JoinType;

/**
 * 클럽 생성 요청에 필요한 데이터를 담는 DTO
 *
 * <p> 클럽 생성 시 필수 및 선택 정보를 포함합니다.
 * - 클럽 이름, 타입, 카테고리
 * - 점수 제한, 최대 인원수
 * - 소개말, 설명, 포스터 이미지 URL
 * - 공개 여부 및 가입 방식
 * <p>
 * 각 필드는 유효성 검사를 통해 적절한 값이 입력되도록 제한합니다.
 */

@Getter
public class CreateClubRequestDto {
	@Size(min = 2, max = 20, message = "클럽 이름은 2~20 글자 사이만 가능합니다.")
	private String name;

	@NotNull(message = "클럽 타입은 필수값입니다.")
	private ClubType clubType;

	@NotNull(message = "카테고리 타입은 필수값입니다.")
	private CategoryType categoryDetail;

	@Min(value = 0, message = "점수 제한은 0점 이상이어야 합니다.")
	@Max(value = 10000, message = "점수 제한은 10,000점 이하여야 합니다.")
	private int minScoreRequirement;

	@Min(value = 2, message = "인원수는 2명 이상이어야 합니다.")
	@Max(value = 10, message = "인원수는 10명 이하여야 합니다.")
	private int maxMemberCount;

	@Size(min = 2, max = 100, message = "소개말은 2자 이상 100자 이하로 작성해주세요.")
	private String greeting;

	@Size(min = 2, max = 255, message = "설명은 2자 이상 255자 이하로 작성해주세요.")
	private String description;

	@NotBlank(message = "이미지는 필수값입니다.")
	private String posterUrl;

	@NotNull(message = "공개여부는 반드시 true 또는 false 여야 합니다.")
	private Boolean isPublic;

	@NotNull(message = "가입 방식은 필수값입니다.")
	private JoinType joinType;
}
