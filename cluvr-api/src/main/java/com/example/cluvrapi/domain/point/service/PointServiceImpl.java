package com.example.cluvrapi.domain.point.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.point.dto.request.CreatePointRequestDto;
import com.example.cluvrapi.domain.point.dto.response.CreatePointResponseDto;
import com.example.cluvrapi.domain.point.dto.response.FindPointLogResponseDto;
import com.example.cluvrapi.domain.point.repository.PointLogRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

@Repository
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final UserRepository userRepository;
	private final PointLogRepository pointLogRepository;

	@Transactional
	@Override
	public CreatePointResponseDto chargePoint(Long userId, CreatePointRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		user.updatePoint(requestDto.getAmount());
		return CreatePointResponseDto.from(user.getPoint());
	}

	@Transactional(readOnly = true)
	@Override
	public List<FindPointLogResponseDto> findPointLogByUserId(Long userId) {
		return pointLogRepository.findPointLogByUserId(userId);
	}
}
