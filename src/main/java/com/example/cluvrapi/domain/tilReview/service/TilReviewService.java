package com.example.cluvrapi.domain.tilReview.service;

import com.example.cluvrapi.global.annotation.IsClubAdmin;
import com.example.cluvrapi.global.exception.BusinessException;

public interface TilReviewService {

	/**
	 * 특정 TIL에 대해 리뷰 요청을 수행하는 메서드
	 *
	 * @param userId 유저 고유 식별자
	 * @param clubId 클럽 고유 식별자
	 * @param tilId  리뷰를 요청할 TIL의 고유 식별자
	 * @throws BusinessException 존재하지 않는 클럽, TIL 또는 권한 없음 등의 경우 404 NotFound 또는 403 Forbidden 예외 발생
	 * @author sinyoung0403
	 */

	@IsClubAdmin
	void requestReview(Long userId, Long clubId, Long tilId);
}
