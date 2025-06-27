package com.example.cluvrapi.domain.clubMember.repository;

import static com.example.cluvrapi.domain.clubMember.entity.QClubMember.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.club.entity.Club;
import com.example.cluvrapi.domain.clubMember.entity.ClubMember;
import com.example.cluvrapi.domain.clubMember.entity.QClubMember;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberRole;
import com.example.cluvrapi.domain.clubMember.entity.enums.ClubMemberStatus;
import com.example.cluvrapi.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ClubMemberRepositoryImpl implements ClubMemberRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<ClubMember> findByClubAndUserWithAnyStatus(Club club, User user) {
		QClubMember cm = clubMember;
		return Optional.ofNullable(queryFactory.selectFrom(cm).where(cm.club.eq(club), cm.user.eq(user)).fetchOne());
	}

	@Override
	public Optional<ClubMember> findOwnerByClub(Club club) {
		QClubMember cm = clubMember;
		return Optional.ofNullable(queryFactory.selectFrom(cm)
			.where(cm.club.eq(club), cm.clubMemberRole.eq(ClubMemberRole.OWNER),
				cm.clubMemberStatus.eq(ClubMemberStatus.ACTIVE))
			.fetchOne());
	}

	@Override
	public Optional<ClubMember> findByClubIdAndUserId(Long clubId, Long userId) {
		QClubMember cm = clubMember;
		return Optional.ofNullable(
			queryFactory.selectFrom(cm).where(cm.club.id.eq(clubId), cm.user.id.eq(userId)).fetchOne());
	}

	@Override
	public Page<ClubMember> findActiveMembersByClubId(Long clubId, Pageable pageable) {
		QClubMember cm = clubMember;

		List<ClubMember> content = queryFactory.selectFrom(cm)
			.where(cm.club.id.eq(clubId), cm.clubMemberStatus.eq(ClubMemberStatus.ACTIVE))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory.select(cm.count())
			.from(cm)
			.where(cm.club.id.eq(clubId), cm.clubMemberStatus.eq(ClubMemberStatus.ACTIVE))
			.fetchOne();

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public long countByClubIdAndStatus(Long clubId, ClubMemberStatus status) {
		Long cnt = queryFactory
			.select(clubMember.count())
			.from(clubMember)
			.where(
				clubMember.club.id.eq(clubId),
				clubMember.clubMemberStatus.eq(status)
			)
			.fetchOne();
		return Objects.requireNonNullElse(cnt, 0L);
	}

	@Override
	public List<ClubMember> findActiveClubMembershipsByUserId(Long userId) {
		QClubMember cm = clubMember;
		return queryFactory.selectFrom(cm)
			.where(cm.user.id.eq(userId), cm.clubMemberStatus.eq(ClubMemberStatus.ACTIVE))
			.fetch();
	}
}
