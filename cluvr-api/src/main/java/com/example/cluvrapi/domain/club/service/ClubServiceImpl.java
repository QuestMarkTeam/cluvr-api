package com.example.cluvrapi.domain.club.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.category.entity.Category;
import com.example.cluvrapi.domain.category.enums.CategoryTargetType;
import com.example.cluvrapi.domain.category.repository.CategoryRepository;
import com.example.cluvrapi.domain.club.dto.request.CreateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpdateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpgradeMemberCountRequestDto;
import com.example.cluvrapi.domain.club.dto.response.CreateClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.CreateInviteCodeResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.repository.ClubRepository;
import com.example.cluvrapi.domain.clubMember.entity.ClubMember;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.clubMember.repository.ClubMemberRepository;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

	private final UserRepository userRepository;
	private final ClubRepository clubRepository;
	private final CategoryRepository categoryRepository;
	private final ClubRedisService clubRedisService;

	private final ClubMemberRepository clubMemberRepository;
	// 상수 선언
	private static final int FREE_LIMIT = 20;
	private static final int GEM_INCREMENT = 5;
	private static final String INVITE_CODE_KEY_PREFIX = "ic:";
	private static final Duration EXPIRE_TTL_TIME = Duration.ofDays(3);

	@Override
	@Transactional
	public CreateClubResponseDto createClub(Long userId, CreateClubRequestDto createClubRequestDto) {
		User findUser = userRepository.findByIdOrElseThrow(userId);

		Club newClub = new Club(
			createClubRequestDto.getName(),
			createClubRequestDto.getClubType(),
			createClubRequestDto.getMaxMemberCount(),
			createClubRequestDto.getMinScoreRequirement(),
			createClubRequestDto.getGreeting(),
			createClubRequestDto.getDescription(),
			createClubRequestDto.getPosterUrl(),
			createClubRequestDto.getIsPublic(),
			createClubRequestDto.getJoinType(),
			findUser
		);

		clubRepository.save(newClub);

		Category newCategory = new Category(
			newClub.getId(),
			createClubRequestDto.getCategoryDetail(),
			CategoryTargetType.CLUB
		);

		ClubMember ownerMember = new ClubMember(
			newClub,
			findUser,
			ClubMemberRole.OWNER,
			ClubMemberStatus.ACTIVE
		);
		clubMemberRepository.save(ownerMember);

		categoryRepository.save(newCategory);

		return CreateClubResponseDto.from(newClub.getId());
	}

	@Override
	public FindClubResponseDto findClubById(Long clubId) {
		return clubRepository.findClubById(clubId);
	}

	@Override
	public PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable) {
		return clubRepository.findAllClub(clubType, pageable);
	}

	@Override
	@Transactional
	public void updateClub(Long clubId, UpdateClubRequestDto updateClubRequestDto) {
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		if (updateClubRequestDto.getName() != null) {
			findClub.updateName(updateClubRequestDto.getName());
		}

		if (updateClubRequestDto.getGreeting() != null) {
			findClub.updateGreeting(updateClubRequestDto.getGreeting());
		}

		if (updateClubRequestDto.getDescription() != null) {
			findClub.updateDescription(updateClubRequestDto.getDescription());
		}
	}

	@Override
	public void deleteClub(Long clubId) {
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		clubRepository.delete(findClub);
	}

	@Override
	@Transactional
	public void upgradeMemberCount(Long userId, Long clubId, UpgradeMemberCountRequestDto memberCountRequestDto) {
		// 1) 클럽 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) 로그인한 유저와 조회한 클럽의 마스터가 일치하는지 검증
		if (findClub.getUser().getId() != userId) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED);
		}

		// 3) 인원 검증

		int requested = memberCountRequestDto.getMemberCount();

		if (requested <= 0) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "추가 인원은 1명 이상이어야 합니다.");
		}

		if (findClub.getMaxMemberCount() + requested > FREE_LIMIT) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST,
				"최대 인원 수는 20명을 초과할 수 없습니다. 추가 인원을 원하실 경우 잼(Gem)을 사용해 확장해 주세요.");
		}

		// 4) 추가
		findClub.upgradeMemberCount(memberCountRequestDto.getMemberCount());
	}

	@Override
	@Transactional
	public void upgradeMemberCountWithGem(Long userId, Long clubId) {
		// 1) 클럽 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) 로그인한 유저와 조회한 클럽의 마스터가 일치하는지 검증
		if (findClub.getUser().getId() != userId) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED);
		}

		// 3) 인원 검증
		if (findClub.getMaxMemberCount() < FREE_LIMIT) {
			throw new BusinessException(
				ResponseCode.INVALID_REQUEST,
				"최대 20명까지는 무료로 확장할 수 있습니다. 이후 인원 추가를 원하시면 잼(Gem)을 사용해 확장해 주세요.");
		}

		// 4) 추가
		findClub.upgradeMemberCount(GEM_INCREMENT);
	}

	@Override
	@Transactional
	public CreateInviteCodeResponseDto createInviteCode(Long userId, Long clubId) {
		// 1) 클럽 조회 및 권한 검증
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		if (findClub.getUser().getId().equals(userId)) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED, "클럽장만이 초대코드를 생성할 수 있습니다.");
		}

		// 2) Base64 로 초대코드 생성
		String code;
		String key;
		SecureRandom random = new SecureRandom();
		do {
			byte[] bytes = new byte[10];
			random.nextBytes(bytes);
			code = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, 12);
			key = INVITE_CODE_KEY_PREFIX + code;
		} while (clubRedisService.hasKey(key));

		// 4) Redis 저장
		Map<String, Object> codeData = new HashMap<>();
		codeData.put("clubId", clubId);
		codeData.put("createdAt", LocalDateTime.now().toString());

		clubRedisService.saveInviteCode(key, codeData, EXPIRE_TTL_TIME);

		// 5) 반환
		return CreateInviteCodeResponseDto.from(code);
	}
}
