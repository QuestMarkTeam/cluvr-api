package com.example.cluvrapi.domain.clubMember.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.clubMember.dto.response.ClubMemberInfoResponseDto;
import com.example.cluvrapi.domain.clubMember.dto.response.GetMemberRoleResponseDto;
import com.example.cluvrapi.domain.clubMember.entity.ClubMember;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.join.entity.JoinRequest;
import com.example.cluvrapi.domain.join.enums.JoinStatus;
import com.example.cluvrapi.domain.join.repository.JoinRequestRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {

	private final JoinRequestRepository joinRequestRepository;
	private final ClubMemberRepository clubMemberRepository;
	private final ClubRepository clubRepository;

	@Transactional
	@Override
	public void handleJoinRequest(Long clubId, Long joinRequestId, JoinStatus status, AuthUser approver) {
		JoinRequest jr = joinRequestRepository.joinRequestByIdAndClubId(joinRequestId, clubId)
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "가입 요청을 찾을 수 없습니다."));

		if (jr.getJoinStatus() != JoinStatus.PENDING) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 처리된 요청입니다.");
		}

		ClubMember approverMember = clubMemberRepository.findByClubIdAndUserId(clubId, approver.id())
			.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_REQUEST, "권한이 없습니다."));
		if (approverMember.getClubMemberRole() != ClubMemberRole.OWNER
			&& approverMember.getClubMemberRole() != ClubMemberRole.ADMIN) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "승인 권한이 없습니다.");
		}

		Club club = jr.getClub();
		long activeCount = clubMemberRepository.countByClubIdAndStatus(clubId, ClubMemberStatus.ACTIVE);
		if (activeCount >= club.getMaxMemberCount()) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽 최대 인원을 초과했습니다.");
		}
		var user = jr.getUser();

		if (status == JoinStatus.APPROVED) {
			Optional<ClubMember> existing = clubMemberRepository.findByClubAndUserWithAnyStatus(club, user);

			if (existing.isPresent()) {
				ClubMember cm = existing.get();
				switch (cm.getClubMemberStatus()) {
					case ACTIVE:
						throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 가입된 유저입니다.");
					case KICKED:
						throw new BusinessException(ResponseCode.INVALID_REQUEST, "강퇴된 유저는 재가입할 수 없습니다.");
					case WITHDRAWN:
						cm.rejoin();
						break;
				}
			} else {
				ClubMember nm = new ClubMember(club, user, ClubMemberRole.MEMBER, ClubMemberStatus.ACTIVE);
				clubMemberRepository.save(nm);
			}

			jr.approve();

		} else if (status == JoinStatus.REJECTED) {
			jr.reject();
		} else {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "승인 또는 거절만 가능합니다.");
		}
	}

	@Override
	@Transactional
	public void changeMemberRole(Long clubId, AuthUser authUser, Long targetMemberId, ClubMemberRole newRole) {
		Club club = clubRepository.findByIdOrElseThrow(clubId);

		Long operatorUserId = authUser.id();

		// operator(요청자)의 ClubMember 엔티티 조회
		ClubMember operatorMember = clubMemberRepository.findByClubIdAndUserId(clubId, operatorUserId)
			.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_REQUEST, "권한이 없습니다."));

		ClubMember target = clubMemberRepository.findById(targetMemberId)
			.filter(cm -> cm.getClub().getId().equals(clubId))
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "대상 멤버를 찾을 수 없습니다."));

		if (newRole == ClubMemberRole.OWNER) {
			if (operatorMember.getClubMemberRole() != ClubMemberRole.OWNER) {
				throw new BusinessException(ResponseCode.INVALID_REQUEST, "OWNER 권한이 필요합니다.");
			}
			ClubMember currentOwner = clubMemberRepository.findOwnerByClub(club)
				.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "OWNER 정보가 없습니다."));
			currentOwner.changeRole(ClubMemberRole.ADMIN);

			target.changeRole(ClubMemberRole.OWNER);

		} else {
			if (!(operatorMember.getClubMemberRole() == ClubMemberRole.OWNER
				|| operatorMember.getClubMemberRole() == ClubMemberRole.ADMIN)) {
				throw new BusinessException(ResponseCode.INVALID_REQUEST, "권한이 없습니다.");
			}
			if (operatorMember.getClubMemberRole() == ClubMemberRole.ADMIN && newRole == ClubMemberRole.ADMIN) {
				throw new BusinessException(ResponseCode.INVALID_REQUEST, "ADMIN은 ADMIN으로 변경할 수 없습니다.");
			}
			target.changeRole(newRole);
		}
	}

	@Override
	@Transactional
	public void withdrawFromClub(Long clubId, AuthUser user) {
		ClubMember me = clubMemberRepository.findByClubIdAndUserId(clubId, user.id())
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "가입된 멤버가 아닙니다."));
		if (me.getClubMemberStatus() != ClubMemberStatus.ACTIVE) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 탈퇴 또는 강퇴된 멤버입니다.");
		}
		me.withdraw();
	}

	@Override
	@Transactional
	public void kickMember(Long clubId, AuthUser operator, Long targetMemberId) {
		ClubMember op = clubMemberRepository.findByClubIdAndUserId(clubId, operator.id())
			.orElseThrow(() -> new BusinessException(ResponseCode.INVALID_REQUEST, "권한이 없습니다."));
		if (op.getClubMemberRole() != ClubMemberRole.OWNER && op.getClubMemberRole() != ClubMemberRole.ADMIN) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "ADMIN 이상만 강퇴할 수 있습니다.");
		}

		ClubMember target = clubMemberRepository.findById(targetMemberId)
			.filter(cm -> cm.getClub().getId().equals(clubId))
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "대상 멤버가 없습니다."));

		if (target.getClubMemberStatus() != ClubMemberStatus.ACTIVE) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 탈퇴 또는 강퇴된 멤버입니다.");
		}
		target.kick();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ClubMemberInfoResponseDto> listMembers(Long clubId, AuthUser authUser, Pageable pageable) {

		boolean isMember = clubMemberRepository.findByClubIdAndUserId(clubId, authUser.id())
			.filter(cm -> cm.getClubMemberStatus() == ClubMemberStatus.ACTIVE)
			.isPresent();

		if (!isMember) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽의 ACTIVE 멤버만 조회할 수 있습니다.");
		}

		Page<ClubMember> page = clubMemberRepository.findActiveMembersByClubId(clubId, pageable);

		return page.map(cm -> new ClubMemberInfoResponseDto(cm.getId(), cm.getUser().getId(), cm.getUser().getName(),
			cm.getClubMemberRole()));
	}

	@Override
	public GetMemberRoleResponseDto getMemberRole(Long clubId, Long targetUserId, AuthUser requester) {
		Club club = clubRepository.findByIdOrElseThrow(clubId);

		boolean isActive = clubMemberRepository.findByClubIdAndUserId(clubId, requester.id())
			.filter(cm -> cm.getClubMemberStatus() == ClubMemberStatus.ACTIVE)
			.isPresent();
		if (!isActive) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽의 ACTIVE 멤버만 조회할 수 있습니다.");
		}

		ClubMember target = clubMemberRepository.findByClubIdAndUserId(clubId, targetUserId)
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "대상 멤버를 찾을 수 없습니다."));

		return new GetMemberRoleResponseDto(
			club.getId(),
			club.getName(),
			targetUserId,
			target.getClubMemberRole()
		);
	}

}

