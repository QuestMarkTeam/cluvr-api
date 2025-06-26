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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.cluvrapi.domain.gem.dto.GemUpdateDto;
import com.example.cluvrapi.domain.gem.dto.request.UpdateGemRequestDto;
import com.example.cluvrapi.domain.gem.enums.GemActionType;
import com.example.cluvrapi.domain.gem.enums.GemUserActivityType;
import com.example.cluvrapi.domain.gem.service.GemEvent;
import com.example.cluvrapi.domain.gem.service.GemService;
import com.example.cluvrapi.global.annotation.EventGem;
import com.example.cluvrapi.global.annotation.UpdateGem;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.jwt.CustomUserDetails;
import com.example.cluvrapi.global.response.ResponseCode;

@Aspect
@RequiredArgsConstructor
@Component
public class GemAspect {

	private final GemService gemService; // 내부에서 트랜잭션 공유 가능하도록
	private final ApplicationEventPublisher publisher; // 이벤트 발행

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

	public void setEvent(GemActionType flowType, Integer gem, GemUserActivityType gemUserActivityType){
		LocalDateTime createdTime = flowType.getEventDate();
		LocalDateTime deletedTime = flowType.getDeleteDate();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			Object principal = auth.getPrincipal();
			if (principal instanceof CustomUserDetails userDetails) {
				Long userId = userDetails.getUser().getId();
				// 적립,사용 등 Gem에  +,- 바꿔서 넘겨줌
				if(gemUserActivityType.getFlowType() == GemActionType.EVENT_EARN){
					gemService.earnGems(gemUserActivityType, userId); // 이벤트 포인트 적립 처리
				}else{
					gemService.updateGems(UpdateGemRequestDto.from(userId, flowType.apply(gem), gemUserActivityType));
				}
				publisher.publishEvent(
					GemEvent.createEvent(userId, gem, gemUserActivityType.getDescription(), createdTime, deletedTime,
						gemUserActivityType.getFlowType(), gemUserActivityType.name())
				);
			}else {
				throw new BusinessException(ResponseCode.AUTH_REQUIRED);
			}
		}else {
			throw new BusinessException(ResponseCode.AUTH_REQUIRED);
		}
	}

	public Integer getGem(ProceedingJoinPoint pjp){
		GemUpdateDto earnDto = Arrays.stream(pjp.getArgs())
			.filter(GemUpdateDto.class::isInstance)
			.map(GemUpdateDto.class::cast)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("GemUpdateDto가 필요합니다"));
		return earnDto.getGem();
	}
}
