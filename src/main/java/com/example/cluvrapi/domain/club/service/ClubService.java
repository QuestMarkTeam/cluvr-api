package com.example.cluvrapi.domain.club.service;

import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.club.dto.request.CreateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpdateClubRequestDto;
import com.example.cluvrapi.domain.club.dto.request.UpgradeMemberCountRequestDto;
import com.example.cluvrapi.domain.club.dto.response.CreateClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.CreateInviteCodeResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindAllClubResponseDto;
import com.example.cluvrapi.domain.club.dto.response.FindClubResponseDto;
import com.example.cluvrapi.domain.club.enums.ClubType;
import com.example.cluvrapi.domain.club.enums.JoinType;
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.annotation.IsClubAdmin;
import com.example.cluvrapi.global.annotation.IsClubOwner;
import com.example.cluvrapi.global.exception.BusinessException;

/**
 * 클럽 관련 주요 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 *
 * <p>클럽 생성, 조회, 수정, 삭제, 멤버 수 증가, 초대코드 생성 등
 * 클럽과 관련된 기능을 제공합니다.
 *
 * @author sinyoung0403
 */

public interface ClubService {

	/**
	 * 설명: 클럽을 생성하는 메서드
	 *
	 * <p> 반환 시 클럽 아이디만 반환됩니다.
	 *
	 * @param userId               클럽을 생성하는 유저의 고유 식별자
	 * @param createClubRequestDto 클럽 생성을 위한 정보가 담긴 DTO
	 * @return 생성된 클럽의 ID 정보를 담은 DTO
	 * @author sinyoung0403
	 */
	CreateClubResponseDto createClub(Long userId, CreateClubRequestDto createClubRequestDto);

	/**
	 * 설명: 클럽 단건을 조회하는 메서드
	 *
	 * <p> 클럽 ID로 단일 클럽 정보를 조회합니다.
	 *
	 * @param clubId 조회할 클럽의 고유 식별자
	 * @return 단건 클럽 조회 결과 DTO
	 * @author sinyoung0403
	 */
	FindClubResponseDto findClubById(Long clubId);

	/**
	 * 설명: 클럽 다건을 조회하는 메서드
	 *
	 * <p> 클럽 타입 기준으로 페이징 처리된 클럽 목록을 조회합니다.
	 *
	 * @param clubType 클럽의 타입
	 * @param pageable 페이징 정보
	 * @return 페이징된 클럽 목록 DTO
	 * @author sinyoung0403
	 */
	PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable);

	/**
	 * 설명: 클럽 정보를 수정하는 메서드
	 *
	 * <p> 클럽의 이름, 소개말, 설명을 수정할 수 있습니다.
	 *
	 * @param userId               수정할 클럽의 고유 식별자
	 * @param clubId               수정할 클럽의 고유 식별자
	 * @param updateClubRequestDto 수정할 클럽 정보가 담긴 DTO
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void updateClub(Long userId, Long clubId, UpdateClubRequestDto updateClubRequestDto);

	/**
	 * 설명: 클럽을 삭제하는 메서드
	 *
	 * <p> 클럽 삭제 시 Soft Delete가 적용됩니다.
	 *
	 * @param userId 삭제할 클럽의 고유 식별자
	 * @param clubId 삭제할 클럽의 고유 식별자
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void deleteClub(Long userId, Long clubId);

	/**
	 * 설명: 클럽의 제한 인원수를 추가하는 메서드
	 *
	 * <p> 무료로 인원수를 증가 시킬 수 있습니다.
	 *
	 * @param userId 유저 고유 식별자
	 * @param clubId 클럽 고유 식별자
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void upgradeMemberCount(Long userId, Long clubId, UpgradeMemberCountRequestDto upgradeMemberCountRequestDto);

	/**
	 * 설명: Gem 을 이용하여 클럽의 제한 인원수를 추가하는 메서드
	 *
	 * <p> Gem 을 사용해야 합니다.
	 *
	 * @param userId 유저 고유 식별자
	 * @param clubId 클럽 고유 식별자
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void upgradeMemberCountWithGem(Long userId, Long clubId);

	/**
	 * 설명: 클럽 초대코드를 생성하는 메서드
	 *
	 * <p> 해당 메서드는 클럽장 권한을 가진 사용자가 초대코드를 발급할 때 사용됩니다.
	 * 이미 초대코드가 존재할 경우, 기존 코드를 무효화하고 새로 생성합니다.
	 *
	 * @param userId 초대코드를 생성하려는 유저의 고유 식별자 (클럽장)
	 * @param clubId 초대코드를 생성할 클럽의 고유 식별자
	 * @return 생성된 초대코드 정보를 담은 DTO
	 * @throws BusinessException {ResponseCode.NOT_FOUND}
	 * @author sinyoung0403
	 */

	@IsClubAdmin
	CreateInviteCodeResponseDto createInviteCode(Long userId, Long clubId);

	/**
	 * 설명: 클럽을 공개 상태로 전환하는 메서드
	 *
	 * @param userId 현재 요청을 수행하는 사용자 ID
	 * @param clubId 공개 상태로 전환할 클럽의 ID
	 * @throws BusinessException {ResponseCode.NOT_FOUND}
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void updatePrivacy(Long userId, Long clubId, Boolean isPublic);

	/**
	 * 설명: 클럽의 가입 방식을 변경하는 메서드
	 *
	 * @param userId   클럽 가입 방식을 변경하려는 사용자의 ID (권한 검증에 사용)
	 * @param clubId   가입 방식이 변경될 클럽의 ID
	 * @param joinType 변경할 새로운 가입 방식 (JoinType enum)
	 * @throws BusinessException 권한이 없거나 클럽 상태가 유효하지 않을 경우 발생할 수 있음
	 * @author sinyoung0403
	 */

	@IsClubOwner
	void updateJoinType(Long userId, Long clubId, JoinType joinType);
}
