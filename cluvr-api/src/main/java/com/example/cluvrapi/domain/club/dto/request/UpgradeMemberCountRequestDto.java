package com.example.cluvrapi.domain.club.dto.request;

import jakarta.validation.constraints.Max;

import lombok.Getter;

@Getter
public class UpgradeMemberCountRequestDto {
	@Max(value = 1, message = "멤버 수 추가는 최소 1부터 입력해주셔야 합니다.")
	@Max(value = 18, message = "멤버 수 제한은 총 20명입니다. 이 이상은 Point 로 업그레이드 가능합니다.")
	private int memberCount;
}
