package com.example.cluvrapi.global.config;

import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.gem.enums.GemType;
import com.example.cluvrapi.domain.gem.service.GemService;
import com.example.cluvrapi.global.annotation.EarnGem;
import com.example.cluvrapi.global.jwt.CustomUserDetails;
import com.example.cluvrapi.global.listener.dto.GemLogDto;
import com.example.cluvrapi.global.listener.dto.UserEventDto;
import com.example.cluvrapi.global.listener.enums.RedisKey;
import com.example.cluvrapi.global.listener.enums.UserEventType;

@Aspect
@RequiredArgsConstructor
@Component
public class GemAspect {

	private final GemService gemService; // 내부에서 트랜잭션 공유 가능하도록
	private final ApplicationEventPublisher publisher; // 이벤트 발행

	@Around("@annotation(com.example.cluvrapi.global.annotation.EarnGem)")
	public Object afterSuccess(ProceedingJoinPoint pjp) throws Throwable {

		Object result = pjp.proceed();

		//어노테이션 가져와서 포인트 값 가져옴
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		Method method = signature.getMethod();
		EarnGem earnGem = method.getAnnotation(EarnGem.class);
		GemType gemType = earnGem.value();

		// 시큐리티에 인증된 유저 정보
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			if (principal instanceof CustomUserDetails userDetails) {
				Long userId = userDetails.getUser().getId();
				gemService.earnGems(userId, gemType);
				publisher.publishEvent(new UserEventDto<>(
						userId,
						RedisKey.GEM_LOG,
						UserEventType.GEM,
						GemLogDto.of(
							userId,
							gemType.getAmount(),
							gemType.getDescription(),
							gemType.name(),
							gemType.getFlowType()
						)
					)
				);
			}
		}
		return result;
	}

}
