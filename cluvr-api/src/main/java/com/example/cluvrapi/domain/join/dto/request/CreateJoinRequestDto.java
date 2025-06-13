package com.example.cluvrapi.domain.join.dto.request;

import com.example.cluvrapi.domain.club.enums.JoinType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Join Request 에 필요한 정보
 */

@Getter
@NoArgsConstructor
public class CreateJoinRequestDto {
	/**
	 * 클럽의 Join Type 과 가입 신청 할 때의 Join Type 이 다를 수 있기 때문에 받는 것이다.
	 */
	private JoinType joinType;

	/**
	 * 가입 신청 답변
	 */
	@NotBlank
	private String answer;
}
