package com.example.cluvrapi.domain.gem.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.dto.response.FindGemLogResponseDto;
import com.example.cluvrapi.domain.gem.dto.response.UpdateGemResponseDto;
import com.example.cluvrapi.domain.gem.enums.GemType;
import com.example.cluvrapi.domain.gem.repository.GemLogRepository;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Repository
@RequiredArgsConstructor
public class GemServiceImpl implements GemService {

	private final UserRepository userRepository;
	private final GemLogRepository gemLogRepository;
	private final GemRedisService gemRedisService;

	@Transactional
	@Override
	public UpdateGemResponseDto chargeGem(Long userId, UpdateGemRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		user.updateGem(requestDto.getAmount());
		return UpdateGemResponseDto.from(user.getGem()); // 충전 후 포인트 반환
	}

	@Override
	public UpdateGemResponseDto useGem(Long userId, UpdateGemRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		int amount = requestDto.getAmount(); // 감소량
		int currentGem = user.getGem(); // 현재 보유 포인트

		if (currentGem < amount) { // 포인트 부족하면 에러 발생
			throw new BusinessException(ResponseCode.GEM_NOT_ENOUGH);
		}

		user.updateGem(amount);
		return UpdateGemResponseDto.from(currentGem - amount); // 남은 포인트 리턴
	}

	@Transactional
	@Override
	public void earnGems(Long userId, GemType gemType) {
		User user = userRepository.findByIdOrElseThrow(userId);

		String redisKey = "gem:" + gemType.name() + "count";

		Duration ttl = getDurationUntilMidnight();
		Long count = gemRedisService.setIfAbsent(redisKey, 1, ttl) ? 1L : gemRedisService.incrementValue(redisKey);

		// 하루 제한 이하면 적립
		if (gemType.getTodayLimit() >= count) {
			user.updateGem(gemType.getAmount());
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
	public List<FindGemLogResponseDto> findGemLogByUserId(Long userId) {
		return gemLogRepository.findGemLogByUserId(userId);
	}
}
