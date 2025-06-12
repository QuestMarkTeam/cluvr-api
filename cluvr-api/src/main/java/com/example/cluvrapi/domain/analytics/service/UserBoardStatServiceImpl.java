package com.example.cluvrapi.domain.analytics.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBoardStatServiceImpl implements UserBoardStatService {

	@Override
	public <T> List<T> aggregate(String redisKey, Class<T> clazz) {
		// TODO: Redis에서 데이터를 집계하는 로직 구현 필요
		return List.of();
	}

	@Override
	public void deleteStat(Long id) {
		// TODO: 통계 데이터 삭제 로직 구현 필요
		throw new UnsupportedOperationException("아직 구현되지 않음");
	}

	@Override
	public <T> void insertStat(List<T> insertDtoList) {
		// TODO: 통계 데이터 삽입 로직 구현 필요
		throw new UnsupportedOperationException("아직 구현되지 않음");
	}
}
