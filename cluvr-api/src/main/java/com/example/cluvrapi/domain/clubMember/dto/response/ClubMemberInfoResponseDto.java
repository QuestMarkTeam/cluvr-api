package com.example.cluvrapi.domain.clubMember.dto.response;

import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ClubMemberInfoResponseDto {
	private final Long memberId;
	private final Long userId;
	private final String userName;
	private final ClubMemberRole role;
}
