package com.example.cluvrapi.domain.clubMember.dto.response;

import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetMemberRoleResponseDto {
	private final Long clubId;
	private final String clubName;
	private final Long userId;
	private final ClubMemberRole role;

	public static GetMemberRoleResponseDto toDto(
		Long clubId,
		String clubName,
		Long userId,
		ClubMemberRole role
	) {
		return new GetMemberRoleResponseDto(clubId, clubName, userId, role);
	}
}

