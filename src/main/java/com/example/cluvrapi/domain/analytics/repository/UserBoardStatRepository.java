package com.example.cluvrapi.domain.analytics.repository;

import java.util.Optional;

import com.example.cluvrapi.domain.analytics.entity.UserBoardStat;
import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

public interface UserBoardStatRepository extends BaseRepository<UserBoardStat, Long> {
	Optional<UserBoardStat> findByUserId(Long userId);

	default UserBoardStat findByUserIdOrElseThrow(Long userId) {
		return findByUserId(userId).orElseThrow(() -> new BusinessException(ResponseCode.STAT_NOT_ENOUGH));
	}
}
