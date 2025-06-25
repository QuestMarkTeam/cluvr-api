package com.example.cluvrapi.global.config;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

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

import com.example.cluvrapi.domain.clover.dto.CloverUpdateDto;
import com.example.cluvrapi.domain.clover.dto.request.UpdateCloverRequestDto;
import com.example.cluvrapi.domain.clover.enums.CloverActionType;
import com.example.cluvrapi.domain.clover.enums.CloverUserActivityType;
import com.example.cluvrapi.domain.clover.service.CloverEvent;
import com.example.cluvrapi.domain.clover.service.CloverService;
import com.example.cluvrapi.global.annotation.UpdateClover;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Aspect
@RequiredArgsConstructor
@Component
public class CloverAspect {

	private final CloverService cloverService; // 내부에서 트랜잭션 공유 가능하도록
	private final ApplicationEventPublisher publisher; // 이벤트 발행

	@Transactional
	@Around("@annotation(com.example.cluvrapi.global.annotation.UpdateClover)")
	public Object afterSuccess(ProceedingJoinPoint pjp) throws Throwable {

		Object result = pjp.proceed();

		//어노테이션 가져와서 값 가져옴
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		Method method = signature.getMethod();
		UpdateClover updateClover = method.getAnnotation(UpdateClover.class);
		CloverUserActivityType cloverUserActivityType = updateClover.value();
		CloverActionType flowType = cloverUserActivityType.getFlowType();

		// 동적인 값 가져오기
		Integer clover = getCloverData(pjp);
		CloverEarnDto earnDto = Arrays.stream(pjp.getArgs())
			.filter(CloverEarnDto.class::isInstance)
			.map(CloverEarnDto.class::cast)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("CloverEarnDto가 필요합니다"));

		Integer clover = earnDto.getClover();

		LocalDateTime createdTime = (flowType == CloverActionType.EARN) ? LocalDateTime.now() : null;
		LocalDateTime deletedTime = (flowType == CloverActionType.USE) ? LocalDateTime.now() : null;

		// 시큐리티에 인증된 유저 정보
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Jwt jwt) {
			Long userId = jwt.getClaim("custom:userId");

			if (userId == null) {
				throw new BusinessException(ResponseCode.TOKEN_INVALID);
			}
			// 클로버 업데이트
			cloverService.updateClover(new UpdateCloverRequestDto(clover, userId));
			publisher.publishEvent(
				CloverEvent.createEvent(userId, clover, createdTime, deletedTime, cloverUserActivityType));
		} else {
			throw new BusinessException(ResponseCode.AUTH_REQUIRED);
		}
		return result;
	}

	public Integer getCloverData(ProceedingJoinPoint pjp) {
		CloverUpdateDto earnDto = Arrays.stream(pjp.getArgs())
			.filter(CloverUpdateDto.class::isInstance)
			.map(CloverUpdateDto.class::cast)
			.findFirst()
			.orElseThrow(() -> new BusinessException(ResponseCode.FAIL));
		return earnDto.getClover();
	}

}
