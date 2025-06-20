package com.example.cluvrapi.domain.clover.repository;

import java.util.Optional;

import com.example.cluvrapi.domain.clover.entity.Clover;
import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

public interface CloverRepository extends BaseRepository<Clover, Long>, CloverRepositoryQuery {

	public Optional<Clover> findByUserId(Long userId);

	default Clover findByUserIdOrElseThrow(Long userId) {
		return findByUserId(userId).orElseThrow(() -> new BusinessException(ResponseCode.CLOVER_NOT_FOUND));
	}
}
