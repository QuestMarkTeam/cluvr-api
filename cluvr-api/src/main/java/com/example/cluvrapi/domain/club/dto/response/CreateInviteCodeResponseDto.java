package com.example.cluvrapi.domain.club.dto.response;

import lombok.Getter;

/**
 * 클럽 초대코드 생성 응답 DTO
 *
 * <p> 클럽 초대코드 생성 시 생성된 초대코드 정보를 담아 반환합니다.
 *
 * @author sinyoung0403
 */

@Getter
public class CreateInviteCodeResponseDto {

	private String inviteCode;

	public CreateInviteCodeResponseDto(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	/**
	 * 주어진 초대코드 문자열로 CreateInviteCodeResponseDto 객체를 생성합니다.
	 *
	 * @param inviteCode 생성된 초대코드 문자열
	 * @return 초대코드 응답 DTO 객체
	 * @author sinyoung0403
	 */

	public static CreateInviteCodeResponseDto from(String inviteCode) {
		return new CreateInviteCodeResponseDto(inviteCode);
	}
}
