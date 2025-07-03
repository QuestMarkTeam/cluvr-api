package com.example.cluvrapi.domain.join.validator;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.domain.join.enums.JoinStatus;
import com.example.cluvrapi.domain.join.repository.JoinRequestRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class ClubJoinValidator {

	private final JoinRequestRepository joinRequestRepository;
	private final ClubMemberRepository clubMemberRepository;

	public void validateJoinRequest(Club club, User user) {
		validateNoDuplicateJoinRequest(club, user);
		validateNotAlreadyMember(club, user);
		validateNotFullMemberCount(club);
	}

	private void validateNoDuplicateJoinRequest(Club club, User user) {
		joinRequestRepository.findJoinByClubIdAndUserId(club.getId(), user.getId())
			.filter(
				joinRequest -> joinRequest.getJoinStatus() == JoinStatus.PENDING
				|| joinRequest.getJoinStatus() == JoinStatus.APPROVED
			)
			.ifPresent(joinRequest -> {
				throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 가입 신청한 클럽입니다.");
			});
	}

	private void validateNotAlreadyMember(Club club, User user) {
		clubMemberRepository.findByClubAndUserWithAnyStatus(club, user).ifPresent(clubMember -> {
			switch (clubMember.getClubMemberStatus()) {
				case ACTIVE:
					throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 가입 신청한 클럽입니다.");
				case KICKED:
					throw new BusinessException(ResponseCode.INVALID_REQUEST, "강퇴 당한 클럽은 가입 불가합니다.");
			}
		});
	}

	private void validateNotFullMemberCount(Club club) {
		if (clubMemberRepository.countByClubIdAndStatus(club.getId(), ClubMemberStatus.ACTIVE)
			>= club.getMaxMemberCount()) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 회원이 꽉 찼습니다.");
		}
	}
}

