package com.example.cluvrapi.global.config;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.gem.dto.GemUpdateDto;
import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.gem.handler.GemMethodHandler;
import com.example.cluvrapi.domain.gem.service.GemEvent;
import com.example.cluvrapi.global.annotation.EventGem;
import com.example.cluvrapi.global.annotation.UpdateGem;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Aspect
@RequiredArgsConstructor
@Component
public class GemAspect {

	private final ApplicationEventPublisher publisher; // 이벤트 발행

	private final List<GemMethodHandler> handlers;

	/**
	 * 설명: 이벤트와 관련된 Gem 처리
	 *
	 *
	 * @return {반환값에 대한 설명}
	 *
	 * @author dnjs5024
	 */

	@Transactional
	@Around("@annotation(com.example.cluvrapi.global.annotation.EventGem)")
	public Object handleEventGem(ProceedingJoinPoint pjp) throws Throwable {

		Object result = pjp.proceed();

		//어노테이션 가져와서 포인트 값 가져옴
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		Method method = signature.getMethod();
		EventGem eventGem = method.getAnnotation(EventGem.class);
		GemUserActivityType gemUserActivityType = eventGem.value();
		GemActionType flowType = gemUserActivityType.getFlowType();

		// 정적인 값 가져옴
		Integer gem = gemUserActivityType.getGem();
		// 시큐리티에 인증된 유저 정보
		setEvent(flowType, gem, gemUserActivityType);

		return result;
	}

	/**
	 * 설명: 동적으로 들어오는 Gem 값 처리
	 *
	 *
	 * @author dnjs5024
	 */
	@Transactional
	@Around("@annotation(com.example.cluvrapi.global.annotation.UpdateGem)")
	public Object handleGem(ProceedingJoinPoint pjp) throws Throwable {

		Object result = pjp.proceed();

		//어노테이션 가져와서 포인트 값 가져옴
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		Method method = signature.getMethod();
		UpdateGem updateGem = method.getAnnotation(UpdateGem.class);
		GemUserActivityType gemUserActivityType = updateGem.value();
		GemActionType flowType = gemUserActivityType.getFlowType();

		// 동적인 값 가져오기
		Integer gem = getGem(pjp);
		// 시큐리티에 인증된 유저 정보
		setEvent(flowType, gem, gemUserActivityType);

		return result;
	}

	public void setEvent(GemActionType flowType, Integer gem, GemUserActivityType gemUserActivityType) {
		LocalDateTime createdTime = flowType.getEventDate();
		LocalDateTime deletedTime = flowType.getDeleteDate();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			if (principal instanceof Jwt jwt) {
				Long userId = Long.valueOf(jwt.getClaim("custom:userId"));
				// 각자에 알맞는 비지니스 로직으로 보내줌
				execute(gemUserActivityType, userId, gem, flowType);
				publisher.publishEvent(
					GemEvent.createEvent(userId, gem, gemUserActivityType.getDescription(), createdTime, deletedTime,
						gemUserActivityType.getFlowType(), gemUserActivityType.name())
				);
			} else {
				throw new BusinessException(ResponseCode.AUTH_REQUIRED);
			}
		} else {
			throw new BusinessException(ResponseCode.AUTH_REQUIRED);
		}
	}

	public Integer getGem(ProceedingJoinPoint pjp) {
		GemUpdateDto earnDto = Arrays.stream(pjp.getArgs())
			.filter(GemUpdateDto.class::isInstance)
			.map(GemUpdateDto.class::cast)
			.findFirst()
			.orElseThrow(() -> new BusinessException(ResponseCode.FAIL));
		return earnDto.getGem();
	}

	// 해당 액션에 맞는 비지니스 처리 로직으로 보내줌
	public void execute(GemUserActivityType activityType, Long userId, Integer gem, GemActionType flowType) {
		handlers.stream()
			.filter(handler -> handler.supports(flowType))
			.findFirst()
			.orElseThrow(() -> new BusinessException(ResponseCode.FAIL))
			.handle(userId, UpdateGemRequestDto.from(gem, activityType));
	}
}
