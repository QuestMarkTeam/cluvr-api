package com.example.cluvrapi.domain.club.repository;

import com.example.cluvrapi.domain.club.entity.QClub;
import com.querydsl.core.types.dsl.BooleanExpression;

public class ClubQueryFilter {

	/**
	 * 설명: 삭제되지 않은(Soft delete 가 적용되지 않은) 클럽만 조회하는 조건입니다.
	 *
	 * @return 삭제되지 않은 클럽 조건 (isDeleted = false)
	 * @author sinyoung0403
	 */

	public static BooleanExpression notDeleted() {
		return QClub.club.isDeleted.eq(false);
	}

	/**
	 * 공개 상태인 클럽만 조회하는 조건입니다.
	 *
	 * @return 공개 클럽 조건 (isPublic = true)
	 * @author sinyoung0403
	 */

	public static BooleanExpression publicOnly() {
		return QClub.club.isPublic.eq(true);
	}
}
