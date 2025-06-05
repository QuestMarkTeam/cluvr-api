package com.example.cluvrapi.domain.user.repository;

import static com.example.cluvrapi.domain.user.entity.QUser.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.cluvrapi.domain.user.entity.QUser;
import com.example.cluvrapi.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<User> findByEmail(String email) {
		QUser u = user;
		User user = queryFactory
			.selectFrom(u)
			.where(u.email.eq(email))
			.fetchOne();

		return Optional.ofNullable(user);
	}

	@Override
	public Optional<Long> findPointById(Long userId) {
		Long rawPoint = queryFactory
			.select(user.point)
			.from(user)
			.where(user.id.eq(userId))
			.fetchOne();

		return Optional.ofNullable(rawPoint);
	}
}
