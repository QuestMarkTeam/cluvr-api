package com.example.cluvrapi.domain.clubMember.service;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.club.dto.response.MyClubResponseDto;
import com.example.cluvrapi.domain.clubMember.dto.request.HandleJoinStatusRequestDto;
import com.example.cluvrapi.domain.clubMember.dto.response.ClubMemberInfoResponseDto;
import com.example.cluvrapi.domain.clubMember.dto.response.GetMemberRoleResponseDto;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.global.annotation.IsClubAdmin;
import com.example.cluvrapi.global.annotation.IsClubMember;
import com.example.cluvrapi.global.annotation.IsClubOwner;
import com.example.cluvrapi.global.exception.BusinessException;

/**
 * 설명: 클럽 멤버 관련 핵심 비즈니스 로직을 제공하는 서비스 인터페이스입니다.
 *
 * <p>클럽 가입 요청 처리, 멤버 역할 변경, 탈퇴, 강제 추방, 멤버 목록 조회, 멤버 역할 조회 기능을 포함합니다.
 *
 * @author 정승원
 */
public interface ClubMemberService {

	/**
	 * 클럽 가입 요청을 승인하거나 거절합니다.
	 *
	 * 가입 요청의 상태를 변경하며, 승인 시 해당 사용자를 클럽 멤버로 추가합니다.
	 *
	 * @param clubId 처리할 클럽의 ID
	 * @param joinRequestId 승인 또는 거절할 가입 요청의 ID
	 * @param dto 승인 또는 거절 상태 정보를 담은 DTO
	 * @param approver 요청을 처리하는 운영자 정보
	 * @throws BusinessException 요청이 존재하지 않거나 권한이 없을 경우 발생
	 */
	@IsClubAdmin
	void handleJoinRequest(Long clubId, Long joinRequestId, HandleJoinStatusRequestDto dto, AuthUser approver);

	/**
	 * 설명: 클럽 내 특정 멤버의 역할을 변경합니다.
	 *
	 * <p>운영자(operator)가 대상 사용자(targetUserId)의 역할을 newRole로 업데이트합니다.
	 *
	 * @param clubId       설명: 역할 변경이 이루어질 클럽의 식별자
	 * @param operator     설명: 역할 변경을 수행하는 운영자 정보
	 * @param targetUserId 설명: 역할을 변경할 대상 사용자 식별자
	 * @param newRole      설명: 설정할 새로운 역할(ClubMemberRole)
	 * @throws BusinessException 설명: 권한이 없거나 대상 멤버가 존재하지 않을 경우 발생
	 */
	@IsClubAdmin
	void changeMemberRole(Long clubId, AuthUser operator, Long targetUserId,
		@NotNull(message = "변경할 역할을 지정하세요.") ClubMemberRole newRole);

	/**
	 * 설명: 사용자가 스스로 클럽에서 탈퇴합니다.
	 *
	 * <p>클럽 멤버 리스트에서 해당 사용자를 제거합니다.
	 *
	 * @param clubId 설명: 탈퇴할 클럽의 식별자
	 * @param user   설명: 탈퇴 요청을 수행하는 사용자 정보
	 * @throws BusinessException 설명: 클럽 또는 멤버가 존재하지 않거나 상태가 올바르지 않을 경우 발생
	 */
	@IsClubMember
	void withdrawFromClub(Long clubId, AuthUser user);

	/**
	 * 설명: 운영자가 특정 멤버를 클럽에서 강제 추방합니다.
	 *
	 * <p>대상 멤버(targetMemberId)의 상태를 KICKED 등으로 변경합니다.
	 *
	 * @param clubId         설명: 멤버를 추방할 클럽의 식별자
	 * @param operator       설명: 추방을 수행하는 운영자 정보
	 * @param targetMemberId 설명: 추방할 대상 멤버의 사용자 식별자
	 * @throws BusinessException 설명: 권한이 없거나 대상 멤버가 존재하지 않을 경우 발생
	 */
	@IsClubAdmin
	void kickMember(Long clubId, AuthUser operator, Long targetMemberId);

	/**
	 * 설명: 특정 클럽의 활성 멤버 목록을 페이징 처리하여 조회합니다.
	 *
	 * <p>AuthUser의 권한을 확인한 뒤, 해당 클럽의 ACTIVE 멤버 정보를 반환합니다.
	 *
	 * @param clubId   설명: 조회할 클럽의 식별자
	 * @param authUser 설명: 조회 권한을 확인하기 위한 사용자 정보
	 * @param pageable 설명: 페이징 옵션(Page 번호, 크기 등)
	 * @return Page<ClubMemberInfoResponseDto> 설명: 조회된 클럽 멤버 정보가 담긴 페이징 결과
	 * @throws BusinessException 설명: 권한이 없거나 클럽이 존재하지 않을 경우 발생
	 */
	@IsClubMember
	Page<ClubMemberInfoResponseDto> listMembers(Long clubId, AuthUser authUser, Pageable pageable);

	/**
	 * 설명: 클럽 내 특정 사용자의 역할을 조회합니다.
	 *
	 * <p>대상 사용자의 ClubMemberRole 정보를 반환합니다.
	 *
	 * @param clubId       설명: 조회할 클럽의 식별자
	 * @param targetUserId 설명: 역할을 조회할 대상 사용자 식별자
	 * @return GetMemberRoleResponseDto 설명: 클럽 ID, 클럽 이름, 사용자 ID, 역할 정보가 담긴 DTO
	 * @throws BusinessException 설명: 클럽 또는 대상 멤버가 존재하지 않을 경우 발생
	 */
	GetMemberRoleResponseDto getMemberRole(Long clubId, Long targetUserId);

	@IsClubOwner
	void changeOwnership(Long clubId, AuthUser requestUser, Long targetMemberId);

	List<MyClubResponseDto> getClubsByUser(Long userId);
}
