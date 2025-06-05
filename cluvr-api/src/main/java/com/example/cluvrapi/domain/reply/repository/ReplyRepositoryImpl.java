package com.example.cluvrapi.domain.reply.repository;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ReplyRepositoryImpl implements ReplyRepositoryCustom {
	JPAQueryFactory queryFactory;

	public ReplyRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

}
