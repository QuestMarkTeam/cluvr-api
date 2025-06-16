package com.example.cluvrapi.domain.clubMember.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.clubMember.entity.ClubMember;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.user.entity.User;

public interface ClubMemberRepositoryCustom {
	Optional<ClubMember> findByClubAndUserWithAnyStatus(Club club, User user);

	Optional<ClubMember> findOwnerByClub(Club club);

	Optional<ClubMember> findByClubIdAndUserId(Long clubId, Long userId);

	Page<ClubMember> findActiveMembersByClubId(Long clubId, Pageable pageable);

	long countByClubIdAndStatus(Long clubId, ClubMemberStatus status);

}
