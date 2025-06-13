package com.example.cluvrapi.global.config;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.gem.service.GemEvent;
import com.example.cluvrapi.domain.gem.service.GemService;
import com.example.cluvrapi.global.annotation.EarnGem;
import com.example.cluvrapi.global.jwt.CustomUserDetails;

@Aspect
@RequiredArgsConstructor
@Component
public class GemAspect {

	private final GemService gemService; // 내부에서 트랜잭션 공유 가능하도록
	private final ApplicationEventPublisher publisher; // 이벤트 발행

	@Transactional
	@Around("@annotation(com.example.cluvrapi.global.annotation.EarnGem)")
	public Object afterSuccess(ProceedingJoinPoint pjp) throws Throwable {

		Object result = pjp.proceed();

		//어노테이션 가져와서 포인트 값 가져옴
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		Method method = signature.getMethod();
		EarnGem earnGem = method.getAnnotation(EarnGem.class);
		GemUserActivityType gemUserActivityType = earnGem.value();
		GemActionType flowType = gemUserActivityType.getFlowType();

		Integer gem = flowType.apply(gemUserActivityType.getAmount()); // 음수,양수 바꿈

		LocalDateTime createdTime = flowType.getEventDate();
		LocalDateTime deletedTime = flowType.getDeleteDate();

		// 시큐리티에 인증된 유저 정보
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			if (principal instanceof CustomUserDetails userDetails) {
				Long userId = userDetails.getUser().getId();
				gemService.earnGems(userId, gemUserActivityType);
				publisher.publishEvent(
					GemEvent.createEvent(userId, gem, gemUserActivityType.getDescription(), createdTime, deletedTime,
						gemUserActivityType.getFlowType(), gemUserActivityType.name())
				);
			}
		}
		return result;
	}

}
