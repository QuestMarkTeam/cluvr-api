package com.example.cluvrapi.domain.point.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.point.dto.request.UpdatePointRequestDto;
import com.example.cluvrapi.domain.point.dto.response.FindPointLogResponseDto;
import com.example.cluvrapi.domain.point.dto.response.UpdatePointResponseDto;
import com.example.cluvrapi.domain.point.enums.PointType;
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
	private final PointRedisService pointRedisService;

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

	@Transactional
	@Override
	public void earnPoints(Long userId, PointType pointType) {
		User user = userRepository.findByIdOrElseThrow(userId);

		String redisKey = "point:" + pointType.name() + "count";

		Duration ttl = getDurationUntilMidnight();
		Long count = pointRedisService.setIfAbsent(redisKey, 1, ttl) ? 1L : pointRedisService.incrementValue(redisKey);

		// 하루 제한 이하면 적립
		if (pointType.getTodayLimit() >= count) {
			user.updatePoint(pointType.getAmount());
		}
	}

	// 현재시간
	public Duration getDurationUntilMidnight() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay(); // 내일 자정시간
		return Duration.between(now, midnight); // 내일 자정까지 남은 시간 계산해서 리턴
	}

	@Transactional(readOnly = true)
	@Override
	public List<FindPointLogResponseDto> findPointLogByUserId(Long userId) {
		return pointLogRepository.findPointLogByUserId(userId);
	}
}
