package com.example.cluvrapi.domain.clubMember.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.clubMember.dto.request.HandleJoinStatusRequestDto;
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
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;
import com.example.cluvrapi.domain.club.dto.response.MyClubResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubMemberServiceImpl implements ClubMemberService {

	private final JoinRequestRepository joinRequestRepository;
	private final ClubMemberRepository clubMemberRepository;
	private final ClubRepository clubRepository;
	private final UserRepository userRepository;

	@Transactional
	@Override
	public void handleJoinRequest(Long clubId, Long joinRequestId, HandleJoinStatusRequestDto dto, AuthUser approver) {
		// 1. 요청 정보 조회
		JoinRequest joinRequest = joinRequestRepository.joinRequestByIdAndClubId(clubId, joinRequestId)
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "가입 요청을 찾을 수 없습니다."));

		// 2. 가입 요청 상태 검증
		if (joinRequest.getJoinStatus() != JoinStatus.PENDING) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 처리된 요청입니다.");
		}

		// 3. 클럽, 유저, DTO 정보 추출
		Club club = joinRequest.getClub();
		User user = joinRequest.getUser();
		JoinStatus status = dto.getStatus();

		// 4. 승인 또는 거절 처리
		switch (status) {
			case APPROVED -> {
				Long activeCount = clubMemberRepository.countByClubIdAndStatus(clubId, ClubMemberStatus.ACTIVE);
				if (activeCount >= club.getMaxMemberCount()) {
					throw new BusinessException(ResponseCode.INVALID_REQUEST, "클럽 최대 인원을 초과했습니다.");
				}
				handleApproval(club, user, joinRequest);
			}
			case REJECTED -> joinRequest.reject();
			default -> throw new BusinessException(ResponseCode.INVALID_REQUEST, "승인 또는 거절만 가능합니다.");
		}
	}


	@Override
	@Transactional
	public void changeMemberRole(Long clubId,
		AuthUser authUser,
		Long targetMemberId,
		ClubMemberRole newRole) {
		// OWNER 위임은 별도 API(/transfer-owner)로만 허용
		if (newRole == ClubMemberRole.OWNER) {
			throw new BusinessException(
				ResponseCode.INVALID_REQUEST,
				"OWNER 권한 변경은 transferOwner API를 이용하세요."
			);
		}

		Club club = clubRepository.findByIdOrElseThrow(clubId);

		// 현재 소유자 조회 (ADMIN은 OWNER 전환 자체가 차단되므로 이 라인은 OWNER 전환이 아닐 때만 사용)
		ClubMember currentOwner = clubMemberRepository.findOwnerByClub(club)
			.orElseThrow(() -> new BusinessException(
				ResponseCode.NOT_FOUND,
				"OWNER 정보가 없습니다."
			));

		ClubMember target = clubMemberRepository.findById(targetMemberId)
			.filter(cm -> cm.getClub().getId().equals(clubId))
			.orElseThrow(() -> new BusinessException(
				ResponseCode.NOT_FOUND,
				"대상 멤버를 찾을 수 없습니다."
			));

		// ADMIN은 ADMIN으로 변경 불가
		if (currentOwner.getClubMemberRole() == ClubMemberRole.ADMIN
			&& newRole == ClubMemberRole.ADMIN) {
			throw new BusinessException(
				ResponseCode.INVALID_REQUEST,
				"ADMIN은 ADMIN으로 변경할 수 없습니다."
			);
		}

		target.changeRole(newRole);
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

		// JoinRequest SoftDeleted 적용
		JoinRequest joinRequest = joinRequestRepository.findJoinByClubIdAndUserId(clubId, user.id()).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "가입된 요청이 없습니다.")
		);

		joinRequest.delete();
	}

	@Override
	@Transactional
	public void kickMember(Long clubId, AuthUser operator, Long targetMemberId) {

		ClubMember target = clubMemberRepository.findById(targetMemberId)
			.filter(cm -> cm.getClub().getId().equals(clubId))
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "대상 멤버가 없습니다."));

		if (target.getClubMemberStatus() != ClubMemberStatus.ACTIVE) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 탈퇴 또는 강퇴된 멤버입니다.");
		}

		target.kick();

		// JoinRequest SoftDeleted 적용
		JoinRequest joinRequest = joinRequestRepository.findJoinByClubIdAndUserId(clubId, target.getUser().getId()).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "가입된 요청이 없습니다.")
		);

		joinRequest.delete();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ClubMemberInfoResponseDto> listMembers(Long clubId, AuthUser authUser, Pageable pageable) {


		Page<ClubMember> page = clubMemberRepository.findActiveMembersByClubId(clubId, pageable);

		return page.map(cm -> new ClubMemberInfoResponseDto(cm.getId(), cm.getUser().getId(), cm.getUser().getName(),
			cm.getClubMemberRole()));
	}

	@Override
	public GetMemberRoleResponseDto getMemberRole(Long clubId, Long targetUserId) {
		Club club = clubRepository.findByIdOrElseThrow(clubId);
		User findUser = userRepository.findByIdOrElseThrow(targetUserId);

		ClubMember target = clubMemberRepository.findByClubIdAndUserId(clubId, targetUserId)
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "대상 멤버를 찾을 수 없습니다."));

		return GetMemberRoleResponseDto.toDto(
			findUser.getId(),
			findUser.getName(),
			club.getId(),
			club.getName(),
			target.getClubMemberRole()
		);
	}

	@Override
	@Transactional
	public void changeOwnership(Long clubId, AuthUser requestUser, Long targetMemberId) {
		Club club = clubRepository.findByIdOrElseThrow(clubId);

		ClubMember currentOwner = clubMemberRepository.findOwnerByClub(club)
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "현재 OWNER 정보가 없습니다."));

		ClubMember target = clubMemberRepository.findById(targetMemberId)
			.filter(cm -> cm.getClub().getId().equals(clubId))
			.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND, "위임할 멤버를 찾을 수 없습니다."));

		if (target.getClubMemberStatus() != ClubMemberStatus.ACTIVE ||
			target.getClubMemberRole() != ClubMemberRole.MEMBER) {
			throw new BusinessException(
				ResponseCode.INVALID_REQUEST,
				"ACTIVE MEMBER만 OWNER로 위임할 수 있습니다."
			);
		}

		currentOwner.changeRole(ClubMemberRole.ADMIN);
		target.changeRole(ClubMemberRole.OWNER);
	}

	/**
	 * 설명: 사용자가 속한 클럽 목록을 조회하는 API 입니다.
	 *
	 * <p> 요청한 사용자가 속한 클럽 목록을 조회합니다.
	 *
	 * @param userId 사용자 ID
	 * @return 클럽 목록 응답 DTO
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MyClubResponseDto> getClubsByUser(Long userId) {
		return clubMemberRepository.findActiveClubMembershipsByUserId(userId)
			.stream()
			.map(ClubMember::getClub)
			.map(MyClubResponseDto::from)
			.collect(Collectors.toList());
	}

	/**
	 * 가입 요청 승인 처리 메서드.
	 * 기존 ClubMember 상태를 확인하여 예외 처리 또는 복귀/신규 생성 후, 가입 요청 상태를 APPROVED 로 변경한다.
	 */

	private void handleApproval(Club club, User user, JoinRequest joinRequest) {
		Optional<ClubMember> clubMemberOpt = clubMemberRepository.findByClubAndUserWithAnyStatus(club, user);

		if (clubMemberOpt.isPresent()) {
			ClubMember cm = clubMemberOpt.get();
			switch (cm.getClubMemberStatus()) {
				case ACTIVE -> throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 가입된 유저입니다.");
				case KICKED -> throw new BusinessException(ResponseCode.INVALID_REQUEST, "강퇴된 유저는 재가입할 수 없습니다.");
				case WITHDRAWN -> cm.rejoin();
			}
		} else {
			ClubMember nm = new ClubMember(club, user, ClubMemberRole.MEMBER, ClubMemberStatus.ACTIVE);
			clubMemberRepository.save(nm);
		}

		joinRequest.approve();
	}
}

