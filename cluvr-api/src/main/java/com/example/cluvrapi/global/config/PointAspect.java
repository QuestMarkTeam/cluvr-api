package com.example.cluvrapi.global.config;

import java.lang.reflect.Method;

import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.point.service.PointService;
import com.example.cluvrapi.global.annotation.EarnPoint;
import com.example.cluvrapi.global.jwt.CustomUserDetails;

@Aspect
@RequiredArgsConstructor
@Component
public class PointAspect {

	private final PointService pointService; // 내부에서 트랜잭션 공유 가능하도록

	@Around("@annotation(com.example.cluvrapi.global.annotation.EarnPoint)")
	public Object afterSuccess(ProceedingJoinPoint pjp) throws Throwable {

		Object result = pjp.proceed();

		//어노테이션 가져와서 포인트 값 가져옴
		MethodSignature signature = (MethodSignature)pjp.getSignature();
		Method method = signature.getMethod();
		EarnPoint earnPoint = method.getAnnotation(EarnPoint.class);

		// 시큐리티에 인증된 유저 정보
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			if (principal instanceof CustomUserDetails userDetails) {
				Long userId = userDetails.getUser().getId();
				pointService.earnPoints(userId, earnPoint.value());
			}
		}
		return result;
	}

}
