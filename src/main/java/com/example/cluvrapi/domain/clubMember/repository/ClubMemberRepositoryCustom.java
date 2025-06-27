package com.example.cluvrapi.domain.clubMember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.clubMember.entity.ClubMember;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.user.entity.User;

/**
 * 설명: 클럽 멤버 관련 커스텀 리포지토리 메서드를 정의하는 인터페이스입니다.
 *
 * @author 정승원
 */
public interface ClubMemberRepositoryCustom {

	/**
	 * 설명: 특정 클럽과 사용자에 해당하는 ClubMember 엔티티를 모든 상태(활성/비활성)에 대해 조회합니다.
	 *
	 * @param club  설명: 조회할 Club 엔티티
	 * @param user  설명: 조회할 User 엔티티
	 * @return Optional<ClubMember> 설명: 조회된 ClubMember(없으면 Optional.empty())
	 */
	Optional<ClubMember> findByClubAndUserWithAnyStatus(Club club, User user);

	/**
	 * 설명: 특정 클럽의 소유자(OWNER 역할)를 조회합니다.
	 *
	 * @param club  설명: 소유자를 조회할 Club 엔티티
	 * @return Optional<ClubMember> 설명: OWNER 역할의 ClubMember(없으면 Optional.empty())
	 */
	Optional<ClubMember> findOwnerByClub(Club club);

	/**
	 * 설명: 클럽 ID와 사용자 ID로 ClubMember 엔티티를 조회합니다.
	 *
	 * @param clubId  설명: 조회할 클럽의 식별자
	 * @param userId  설명: 조회할 사용자의 식별자
	 * @return Optional<ClubMember> 설명: 조회된 ClubMember(없으면 Optional.empty())
	 */
	Optional<ClubMember> findByClubIdAndUserId(Long clubId, Long userId);

	/**
	 * 설명: 특정 클럽의 활성(ACTIVE) 멤버 목록을 페이징 처리하여 조회합니다.
	 *
	 * @param clubId    설명: 멤버를 조회할 클럽의 식별자
	 * @param pageable  설명: 페이징 정보(Page 번호, 크기 등)
	 * @return Page<ClubMember> 설명: 조회된 활성 멤버의 페이징 결과
	 */
	Page<ClubMember> findActiveMembersByClubId(Long clubId, Pageable pageable);

	/**
	 * 설명: 클럽 ID와 상태(ClubMemberStatus)에 해당하는 멤버 수를 카운트합니다.
	 *
	 * @param clubId  설명: 카운트할 클럽의 식별자
	 * @param status  설명: 카운트할 멤버 상태(예: ACTIVE, KICKED)
	 * @return long  설명: 해당 조건을 만족하는 멤버 수
	 */
	long countByClubIdAndStatus(Long clubId, ClubMemberStatus status);

	List<ClubMember> findActiveClubMembershipsByUserId(Long userId);
}
