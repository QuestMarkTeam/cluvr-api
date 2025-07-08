package com.example.cluvrapi.domain.gem.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.dto.request.UseGemRequestDto;
import com.example.cluvrapi.domain.gem.dto.response.FindGemLogResponseDto;
import com.example.cluvrapi.domain.gem.dto.response.UpdateGemResponseDto;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.gem.repository.GemLogRepository;
import com.example.cluvrapi.domain.notification.enums.NotiTargetType;
import com.example.cluvrapi.domain.notification.enums.NotificationType;
import com.example.cluvrapi.domain.notification.event.NotificationEvent;
import com.example.cluvrapi.domain.notification.event.NotificationProducer;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.annotation.UpdateGem;
import com.example.cluvrapi.global.event.enums.RedisKey;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class GemServiceImpl implements GemService {

	private final UserRepository userRepository;
	private final GemLogRepository gemLogRepository;
	private final GemRedisService gemRedisService;
	private final NotificationProducer notificationProducer;

	@Transactional
	@Override
	public UpdateGemResponseDto chargeGem(Long userId, UpdateGemRequestDto requestDto) {
		User user = userRepository.findByIdOrElseThrow(userId);
		Integer finalGem = user.getGem() + requestDto.getGem();
		user.updateGem(finalGem);
		return UpdateGemResponseDto.from(user.getGem()); // 충전 후 포인트 반환
	}


	@Transactional
	@Override
	public UpdateGemResponseDto useGem(Long userId, UpdateGemRequestDto requestDto) {
		return checkGem(userId,requestDto.getGem());
	}

	@UpdateGem(value = GemUserActivityType.CHAT_CREATE)
	@Transactional
	@Override
	public UpdateGemResponseDto useGemExternal(Long userId, UseGemRequestDto requestDto) {
		return checkGem(userId,requestDto.getGem());
	}

	private UpdateGemResponseDto checkGem(Long userId, Integer gem){
		User user = userRepository.findByIdOrElseThrow(userId);// 감소량
		Integer currentGem = user.getGem(); // 현재 보유 포인트

		if (currentGem < gem) { // 포인트 부족하면 에러 발생
			throw new BusinessException(ResponseCode.GEM_NOT_ENOUGH);
		}
		Integer finalGem = currentGem - gem;
		user.updateGem(finalGem);
		return UpdateGemResponseDto.from(finalGem); // 남은 포인트 리턴
	}

	@Transactional
	@Override
	public void earnGems(Long userId, UpdateGemRequestDto updateGemRequestDto) { // 이벤트로 얻는 포인트 처리
		User user = userRepository.findByIdOrElseThrow(userId);
		GemUserActivityType gemUserActivityType = updateGemRequestDto.getGemUserActivityType();

		String redisKey = RedisKey.GEM_GET_LIMIT.getKey() + userId + ":"+gemUserActivityType.name();
		Duration ttl = getDurationUntilMidnight();
		Long count = gemRedisService.setIfAbsent(redisKey, 1L, ttl) ? 1L : gemRedisService.incrementValue(redisKey);

		// 하루 제한 이하면 적립
			if (gemUserActivityType.getTodayLimit() >= count) {
			Integer updateGem = user.getGem() + updateGemRequestDto.getGem();
			user.updateGem(updateGem);
			String content = String.format("Gem %d 점을 획득하였습니다.", updateGemRequestDto.getGem());
			NotificationEvent event = NotificationEvent.from(
				userId,
				NotificationType.POINT,
				content,
				NotiTargetType.USER,
				userId
			);

			notificationProducer.send(event);
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
