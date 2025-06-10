package com.example.cluvrapi.domain.user.repository;

import static com.example.cluvrapi.domain.user.entity.QUser.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<User> findByEmailAndNotDeleted(String email) {
		User u = queryFactory
			.selectFrom(user)
			.where(
				user.email.eq(email)
					.and(user.isDeleted.eq(false))
			)
			.fetchOne();
		return Optional.ofNullable(u);
	}

	@Override
	public Optional<Long> findPointByIdNotDeleted(Long userId) {
		Long point = queryFactory
			.select(user.point)
			.from(user)
			.where(
				user.id.eq(userId)
					.and(user.isDeleted.eq(false))
			)
			.fetchOne();
		return Optional.ofNullable(point);
	}

	@Override
	public Optional<User> findByIdNotDeleted(Long userId) {
		User u = queryFactory
			.selectFrom(user)
			.where(
				user.id.eq(userId)
					.and(user.isDeleted.eq(false))
			)
			.fetchOne();
		return Optional.ofNullable(u);
	}

	@Override
	public List<User> findAllNotDeleted() {
		return queryFactory
			.selectFrom(user)
			.where(user.isDeleted.eq(false))
			.fetch();
	}
}
