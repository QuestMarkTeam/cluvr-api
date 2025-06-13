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
import com.example.cluvrapi.domain.club.enums.JoinType;
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

/**
 * 클럽 관련 비즈니스 로직을 구현하는 서비스 클래스입니다.
 *
 * @author sinyoung0403
 */

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
		// 1) 유저 조회
		User findUser = userRepository.findByIdOrElseThrow(userId);

		// 2) 검증
		if (createClubRequestDto.getName() != null) {
			if (clubRepository.existsByClubName(createClubRequestDto.getName())) {
				throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 존재하는 클럽명입니다.");
			}
		}

		validateCreateClubRequest(createClubRequestDto.getIsPublic(), createClubRequestDto.getJoinType());

		// 3) 클럽 Entity 생성 및 저장
		Club newClub = new Club(
			createClubRequestDto.getName(),
			createClubRequestDto.getClubType(),
			createClubRequestDto.getMaxMemberCount(),
			createClubRequestDto.getMinCloverRequirement(),
			createClubRequestDto.getGreeting(),
			createClubRequestDto.getDescription(),
			createClubRequestDto.getPosterUrl(),
			createClubRequestDto.getIsPublic(),
			createClubRequestDto.getJoinType()
		);

		clubRepository.save(newClub);

		// 4) Category Entity 생성 및 저장
		Category newCategory = new Category(
			newClub.getId(),
			createClubRequestDto.getCategoryDetail(),
			CategoryTargetType.CLUB
		);

		categoryRepository.save(newCategory);

		// 5) 클럽 맴버 Entity 생성 및 저장
		ClubMember ownerMember = new ClubMember(
			newClub,
			findUser,
			ClubMemberRole.OWNER,
			ClubMemberStatus.ACTIVE
		);

		clubMemberRepository.save(ownerMember);

		// 6) DTO 변환
		return CreateClubResponseDto.from(newClub.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public FindClubResponseDto findClubById(Long clubId) {
		return clubRepository.findClubById(clubId);
	}

	@Override
	@Transactional(readOnly = true)
	public PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable) {
		return clubRepository.findAllClub(clubType, pageable);
	}

	@Override
	@Transactional
	public void updateClub(Long userId, Long clubId, UpdateClubRequestDto updateClubRequestDto) {
		// 1) 클럽 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) 클럽 맴버 조회 및 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "잘못된 접근입니다.")
		);

		validateOwnerRole(findClubMember.getClubMemberRole());

		if (updateClubRequestDto.getName() != null && !updateClubRequestDto.getName().equals(findClub.getName())) {
			if (clubRepository.existsByClubName(updateClubRequestDto.getName())) {
				throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 존재하는 클럽명입니다.");
			}
		}

		// 3) 수정
		if (updateClubRequestDto.getName() != null)
			findClub.updateName(updateClubRequestDto.getName());

		if (updateClubRequestDto.getGreeting() != null)
			findClub.updateGreeting(updateClubRequestDto.getGreeting());

		if (updateClubRequestDto.getDescription() != null)
			findClub.updateDescription(updateClubRequestDto.getDescription());

	}

	@Override
	@Transactional
	public void deleteClub(Long userId, Long clubId) {
		// 1) 클럽 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) 클럽 맴버 조회 및 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "잘못된 접근입니다.")
		);

		validateOwnerRole(findClubMember.getClubMemberRole());

		// 3) 삭제
		clubRepository.delete(findClub);
	}

	@Override
	@Transactional
	public void upgradeMemberCount(Long userId, Long clubId, UpgradeMemberCountRequestDto memberCountRequestDto) {
		// 1) 클럽 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		ClubMember clubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "잘못된 접근입니다.")
		);

		// 2) 로그인한 유저와 조회한 클럽의 마스터가 일치하는지 검증
		validateOwnerRole(clubMember.getClubMemberRole());

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

		ClubMember clubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "잘못된 접근입니다.")
		);

		// 2) 로그인한 유저와 조회한 클럽의 마스터가 일치하는지 검증
		validateOwnerRole(clubMember.getClubMemberRole());

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

		ClubMember clubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "잘못된 접근입니다.")
		);

		// 2) 로그인한 유저와 조회한 클럽의 마스터가 일치하는지 검증
		validateOwnerRole(clubMember.getClubMemberRole());

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

	@Override
	@Transactional
	public void updatePrivacy(Long userId, Long clubId, Boolean isPublic) {

		// 1) 파라미터 검증
		if (isPublic == null) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "공개 여부는 필수 값입니다.");
		}

		// 2) 클럽 조회 및 검증
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		if (findClub.getIsPublic().equals(isPublic)) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "이미 해당 공개 상태입니다.");
		}

		// 3) 클럽 맴버 조회 및 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "잘못된 접근입니다.")
		);

		validateOwnerRole(findClubMember.getClubMemberRole());

		// 4) JoinType 과 isPublic 수정
		findClub.updateJoinType(isPublic ? JoinType.SIMPLE_REQUEST : JoinType.INVITE_CODE);
		findClub.updatePrivacy(isPublic);
	}

	@Override
	@Transactional
	public void updateJoinType(Long userId, Long clubId, JoinType joinType) {
		// 1) 클럽 조회
		Club findClub = clubRepository.findByIdOrElseThrow(clubId);

		// 2) 클럽 맴버 조회 및 권한 검증
		ClubMember findClubMember = clubMemberRepository.findByClubIdAndUserId(clubId, userId).orElseThrow(
			() -> new BusinessException(ResponseCode.INVALID_REQUEST, "잘못된 접근입니다.")
		);

		validateOwnerRole(findClubMember.getClubMemberRole());

		// 3) 가입방식 유효한지 검증
		validateCreateClubRequest(findClub.getIsPublic(), joinType);

		// 4) 수정
		findClub.updateJoinType(joinType);
	}

	/**
	 * 설명: 클럽 생성 시 공개 여부와 가입 방식이 유효한 조합인지 검증
	 *
	 * @param isPublic 클럽의 공개 여부
	 * @param joinType 선택한 가입 방식
	 * @throws BusinessException 잘못된 조합일 경우 예외를 던집니다.
	 * @author sinyoung0403
	 */

	public void validateCreateClubRequest(Boolean isPublic, JoinType joinType) {
		if (isPublic == null) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "공개 여부는 필수 값입니다.");
		}

		if (!isPublic && joinType != JoinType.INVITE_CODE) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "비공개 클럽은 초대코드 가입 방식만 가능합니다.");
		}

		if (isPublic && joinType == JoinType.INVITE_CODE) {
			throw new BusinessException(ResponseCode.INVALID_REQUEST, "공개 클럽은 초대코드 가입 방식을 선택할 수 없습니다.");
		}
	}

	/**
	 * 클럽 멤버의 역할이 클럽장(OWNER)인지 검증합니다.
	 *
	 * @param clubMemberRole 클럽 멤버의 역할
	 * @throws BusinessException 클럽장이 아닌 경우 접근 거부 예외를 던집니다.
	 * @author sinyoung0403
	 */

	public void validateOwnerRole(ClubMemberRole clubMemberRole) {
		if (clubMemberRole != ClubMemberRole.OWNER) {
			throw new BusinessException(ResponseCode.ACCESS_DENIED);
		}
	}
}
