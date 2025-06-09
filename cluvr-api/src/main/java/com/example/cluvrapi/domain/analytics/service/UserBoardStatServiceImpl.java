package com.example.cluvrapi.domain.analytics.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBoardStatServiceImpl implements UserBoardStatService {

	@Override
	public <T> List<T> aggregate(String redisKey, Class<T> clazz) {
		return List.of();
	}

	@Override
	public void deleteStat(Long id) {

	}

	@Override
	public <T> void insertStat(List<T> insertDtoList) {

	}
}
