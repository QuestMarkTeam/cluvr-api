package com.example.cluvrapi.domain.til.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.clubMember.entity.ClubMember;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.notification.enums.NotiTargetType;
import com.example.cluvrapi.domain.notification.enums.NotificationType;
import com.example.cluvrapi.domain.notification.event.NotificationEvent;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;
import com.example.cluvrapi.domain.til.dto.reqeust.CreateTilRequestDto;
import com.example.cluvrapi.domain.til.dto.reqeust.UpdateTilRequestDto;
import com.example.cluvrapi.domain.til.dto.response.CreateTilResponseDto;
import com.example.cluvrapi.domain.til.dto.response.InfoTilResponseDto;
import com.example.cluvrapi.domain.til.entity.Til;
import com.example.cluvrapi.domain.til.repository.TilRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class TilServiceImpl implements TilService {

	private final TilRepository tilRepository;
	private final UserRepository userRepository;
	private final ClubRepository clubRepository;
	private final NotificationProducer notificationProducer;
	private final ClubMemberRepository clubMemberRepository;

	@Override
	@Transactional
	public CreateTilResponseDto createTil(Long userId, Long clubId, CreateTilRequestDto createTilRequestDto) {
		// 1) 유저 및 클럽 조회
		User findUser = userRepository.findByIdOrElseThrow(userId);
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) Til Entity 생성
		Til til = new Til(
			findUser,                            // User
			findClub,                            // Club
			createTilRequestDto.getTitle(),        // Title
			createTilRequestDto.getContent()    // Content
		);

		// 3) Til 저장
		tilRepository.save(til);

		// 4) 알림 전송
		ClubMember ownerClubMember = clubMemberRepository.findOwnerByClub(findClub).orElseThrow(
			() -> new BusinessException(ResponseCode.NOT_FOUND, "클럽 소유자를 찾을 수 없습니다.")
		);

		Long ownerUserId = ownerClubMember.getUser().getId();

		if (!ownerUserId.equals(findUser.getId())) { // 자기 자신에게는 알림 안 보냄
			String content = String.format("'%s'님이 클럽에 새로운 TIL을 작성했습니다.", findUser.getName());

			NotificationEvent event = NotificationEvent.from(
				ownerUserId,                           // 수신자: 클럽장
				NotificationType.TIL,                  // 알림 타입: TIL
				content,
				NotiTargetType.CLUB,                   // 대상 타입: CLUB
				clubId                                 // 대상 ID: clubId
			);

			notificationProducer.send(event); // 알림 전송
		}

		return CreateTilResponseDto.from(til.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public InfoTilResponseDto findTilById(Long clubId, Long tilId) {
		return tilRepository.findTilById(clubId, tilId).orElseThrow(
			() -> new BusinessException(ResponseCode.NOT_FOUND, "해당 TIL 이 존재하지 않습니다.")
		);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<InfoTilResponseDto> findAllTilById(Long clubId, Pageable pageable) {
		return tilRepository.findAllTilById(clubId, pageable);
	}

	@Override
	@Transactional
	public void updateTil(Long userId, Long tilId, UpdateTilRequestDto updateTilRequestDto) {
		// 1) Til 조회
		Til findTil = tilRepository.findByIdOrElseThrow(tilId);

		// 2) 권한 검증. 작성자만 수정 가능하다.
		if (!findTil.getUser().getId().equals(userId)) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED, "작성자만 수정 가능합니다.");
		}

		// 2) 제목 업데이트
		if (updateTilRequestDto.getTitle() != null) {
			findTil.updateTitle(updateTilRequestDto.getTitle());
		}

		// 3) 내용 업데이트
		if (updateTilRequestDto.getContent() != null) {
			findTil.updateContent(updateTilRequestDto.getContent());
		}
	}

	@Override
	@Transactional
	public void deleteTil(Long userId, Long clubId, Long tilId) {
		// 1) Til 조회, Club 조회
		Til findTil = tilRepository.findByIdOrElseThrow(tilId);
		clubRepository.findByIdOrElseThrow(clubId);

		// 2) 멤버 조회
		ClubMember clubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST)
		);

		// 3) 권한 검증. 작성자와 클랜 관리자만 삭제 가능하다.
		if (!findTil.getUser().getId().equals(userId)
			&& clubMember.getClubMemberRole() != ClubMemberRole.OWNER
			&& clubMember.getClubMemberRole() != ClubMemberRole.ADMIN
		) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED, "작성자와 클랜 관리자만이 삭제 가능합니다.");
		}

		// 3) Til 삭제
		tilRepository.delete(findTil);
	}
}
