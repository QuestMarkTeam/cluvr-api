package com.example.cluvrapi.domain.club.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.Getter;

/**
 * 클럽 최대 인원수 증가 요청 DTO
 *
 * <p> 클럽 멤버 수를 무료로 추가할 때 사용됩니다.
 * - 추가할 멤버 수는 최소 1명 이상, 최대 18명 이하로 제한됩니다.
 * - 총 인원수 제한(20명)을 고려한 값입니다.
 *
 * @author sinyoung0403
 */

@Getter
public class UpgradeMemberCountRequestDto {
	@Min(value = 1, message = "멤버 수 추가는 최소 1명부터 가능합니다.")
	@Max(value = 18, message = "무료 확장은 최대 18명까지(총 20명) 가능합니다.")
	private int memberCount;
}
