package com.example.cluvrapi.domain.analytics.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.analytics.repository.PointStatisticsRepository;

@Service
@RequiredArgsConstructor
public class PointStatisticsServiceImpl implements PointStatisticsService {
	private final PointStatisticsRepository pointStatisticsRepository;

}
