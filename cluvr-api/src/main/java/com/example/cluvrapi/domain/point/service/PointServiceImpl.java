package com.example.cluvrapi.domain.point.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.point.dto.request.UpdatePointRequestDto;
import com.example.cluvrapi.domain.point.dto.response.FindPointLogResponseDto;
import com.example.cluvrapi.domain.point.dto.response.UpdatePointResponseDto;
import com.example.cluvrapi.domain.point.repository.PointLogRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Repository
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

	private final UserRepository userRepository;
	private final PointLogRepository pointLogRepository;

	@Transactional
	@Override
	public UpdatePointResponseDto chargePoint(Long userId, UpdatePointRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		user.updatePoint(requestDto.getAmount());
		return UpdatePointResponseDto.from(user.getPoint()); // 충전 후 포인트 반환
	}

	@Override
	public UpdatePointResponseDto usePoint(Long userId, UpdatePointRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		int amount = requestDto.getAmount(); // 감소량
		int currentPoint = user.getPoint(); // 현재 보유 포인트

		if (currentPoint < amount) { // 포인트 부족하면 에러 발생
			throw new BusinessException(ResponseCode.POINT_NOT_ENOUGH);
		}

		user.updatePoint(amount);
		return UpdatePointResponseDto.from(currentPoint - amount); // 남은 포인트 리턴
	}

	@Override
	public void earnPoints(Long userId, UpdatePointRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		user.updatePoint(requestDto.getAmount());
	}

	@Transactional(readOnly = true)
	@Override
	public List<FindPointLogResponseDto> findPointLogByUserId(Long userId) {
		return pointLogRepository.findPointLogByUserId(userId);
	}
}
