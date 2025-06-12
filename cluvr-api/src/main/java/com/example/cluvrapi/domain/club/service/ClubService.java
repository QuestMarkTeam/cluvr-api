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
import com.example.cluvrapi.domain.common.dto.PageResponseDto;
import com.example.cluvrapi.global.exception.BusinessException;

public interface ClubService {

	/**
	 * 설명: 클럽을 생성하는 메서드
	 *
	 * <p> {반환 시 클럽 아이디만 반환된다.}
	 *
	 * @param createClubRequestDto {설명: 클럽 생성을 할 때 필요한 정보}
	 * @return CreateClubResponseDto {설명: 클럽 id 반환}
	 * @author {sinyoung0403}
	 */
	CreateClubResponseDto createClub(Long userId, CreateClubRequestDto createClubRequestDto);

	/**
	 * 설명: 클럽 단건 조회하는 메서드
	 *
	 * <p>{}
	 *
	 * @param clubId {설명: 클럽의 고유 식별자}
	 * @return FindClubResponseDto {설명: 단건 조회 반환}
	 * @author {sinyoung0403}
	 */
	FindClubResponseDto findClubById(Long clubId);

	/**
	 * 설명: 클럽 다건 조회하는 메서드
	 *
	 * <p>{페이징 처리가 된다.}
	 *
	 * @param clubType {설명: 클럽 타입}
	 * @return FindAllClubResponseDto {설명: 다건 조회 반환 / 페이징 처리}
	 * @author {sinyoung0403}
	 */
	PageResponseDto<FindAllClubResponseDto> findAllClub(ClubType clubType, Pageable pageable);

	/**
	 * 설명: 클럽 업데이트 하는 메서드
	 *
	 * <p>{이름, 소개말, 설명을 수정할 수 있다.}
	 *
	 * @param clubId               {설명: 클럽 고유 식별자}
	 * @param updateClubRequestDto {설명: 이름, 소개말, 설명 Request}
	 * @author {sinyoung0403}
	 */
	void updateClub(Long clubId, UpdateClubRequestDto updateClubRequestDto);

	/**
	 * 설명: 클럽을 삭제하는 메서드
	 *
	 * <p>{삭제 시, SoftDeleted 적용이 된다.}
	 *
	 * @param clubId {설명: 클럽 고유 식별자}
	 * @author {sinyoung0403}
	 */
	void deleteClub(Long clubId);

	/**
	 * 설명: 클럽의 제한 인원수를 추가하는 메서드
	 *
	 * <p> 무료로 인원수를 증가 시킬 수 있습니다.
	 *
	 * @param userId 유저 고유 식별자
	 * @param clubId 클럽 고유 식별자
	 * @author sinyoung0403
	 */

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
	void upgradeMemberCountWithGem(Long userId, Long clubId);

	/**
	 * 설명: 클럽 초대코드를 생성합니다.
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
	CreateInviteCodeResponseDto createInviteCode(Long userId, Long clubId);
}
