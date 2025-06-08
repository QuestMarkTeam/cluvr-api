package com.example.cluvrapi.domain.analytics.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.analytics.repository.PointStatRepository;

@Service
@RequiredArgsConstructor
public class PointStatServiceImpl implements PointStatService {
	private final PointStatRepository pointStatisticsRepository;

}
