package com.example.cluvrapi.domain.clubMember.dto.response;

import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetMemberRoleResponseDto {
	private final Long userId;
	private final String nickname;
	private final Long clubId;
	private final String clubName;
	private final ClubMemberRole role;

	public static GetMemberRoleResponseDto toDto(
		Long userId,
		String nickname,
		Long clubId,
		String clubName,
		ClubMemberRole role
	) {
		return new GetMemberRoleResponseDto(userId, nickname, clubId, clubName, role);
	}
}

