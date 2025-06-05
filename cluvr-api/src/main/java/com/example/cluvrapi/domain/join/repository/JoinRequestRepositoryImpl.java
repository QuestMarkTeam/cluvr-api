package com.example.cluvrapi.domain.join.repository;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

@RequiredArgsConstructor
public class JoinRequestRepositoryImpl implements JoinRequestRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public boolean existsJoinByClubIdAndUserId() {
		return false;
	}
}
