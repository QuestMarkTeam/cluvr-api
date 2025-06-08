package com.example.cluvrapi.domain.analytics.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.analytics.repository.PointStatRepository;

@Service
@RequiredArgsConstructor
public class PointStatServiceImpl implements PointStatService {
	private final PointStatRepository pointStatisticsRepository;

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
