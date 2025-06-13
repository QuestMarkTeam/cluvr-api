package com.example.cluvrapi.domain.club.dto.response;

import lombok.Getter;

/**
 * 클럽 생성 응답 DTO
 *
 * <p> 클럽 생성 후 생성된 클럽의 고유 식별자(ID)를 반환합니다.
 *
 * @author sinyoung0403
 */

@Getter
public class CreateClubResponseDto {
	private Long id;

	public CreateClubResponseDto(Long id) {
		this.id = id;
	}

	/**
	 * 설명: 주어진 클럽 ID 로부터 CreateClubResponseDto 객체 생성
	 *
	 * @param userId 생성된 클럽의 ID
	 * @return CreateClubResponseDto 인스턴스
	 * @author sinyoung0403
	 */

	public static CreateClubResponseDto from(Long userId) {
		return new CreateClubResponseDto(userId);
	}
}
