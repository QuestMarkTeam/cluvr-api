package com.example.cluvrapi.domain.club.dto.request;

import jakarta.validation.constraints.Size;

import lombok.Getter;

/**
 * 클럽 수정 요청에 필요한 데이터를 담는 DTO
 *
 * <p>클럽 정보 수정 시 사용되며,
 * - 클럽 이름, 소개말, 설명 등의 필드를 포함합니다.
 * <p>
 * 각 필드는 유효성 검사를 통해 적절한 값이 입력되도록 제한합니다.
 * 수정 가능한 필드만 포함되어 있습니다.
 *
 * @author sinyoung0403
 */

@Getter
public class UpdateClubRequestDto {
	@Size(min = 2, max = 20, message = "클럽명은 2자 이상 20자 이하로 작성해주세요.")
	private String name;
	@Size(min = 2, max = 100, message = "소개말은 2자 이상 100자 이하로 작성해주세요.")
	private String greeting;
	@Size(min = 2, max = 255, message = "설명은 2자 이상 255자 이하로 작성해주세요.")
	private String description;
}
