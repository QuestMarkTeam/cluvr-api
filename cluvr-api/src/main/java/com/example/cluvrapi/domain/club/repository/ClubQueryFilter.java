package com.example.cluvrapi.domain.club.repository;

import com.example.cluvrapi.domain.club.entity.QClub;
import com.querydsl.core.types.dsl.BooleanExpression;

public class ClubQueryFilter {
	public static BooleanExpression notDeleted() {
		return QClub.club.isDeleted.eq(false);
	}
}
