package com.example.cluvrapi.domain.join.service.lock;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DirectJoinLockAspect {

	private final RedissonClient redissonClient;
	private final PlatformTransactionManager transactionManager;

	@Value("${lock.direct-join.wait-time:5}")
	private long waitTime;

	@Value("${lock.direct-join.lease-time:10}")
	private long leaseTime;

	@Around("@annotation(directJoinLock)")
	public Object lock(ProceedingJoinPoint joinPoint, DirectJoinLock directJoinLock) throws Throwable {
		// 메서드 시그니처
		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		String[] parameterNames = methodSignature.getParameterNames(); // 파라미터 이름 배열
		Object[] args = joinPoint.getArgs(); // 실제 파라미터 값 배열

		// SpEL 파서와 컨텍스트 준비
		SpelExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		// 파라미터 이름과 값 context에 세팅
		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], args[i]);
		}

		// SpEL 평가
		String clubId = parser.parseExpression(directJoinLock.clubId()).getValue(context, String.class);
		if (clubId == null) {
			throw new IllegalArgumentException("clubId 파라미터를 찾을 수 없습니다.");
		}

		String key = "direct-club-join:" + clubId;

		RLock lock = redissonClient.getLock(key);

		boolean isLocked = false;
		try {
			isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
			if (!isLocked) {
				throw new IllegalStateException("락 획득 실패: key = " + key);
			}

			log.info("락 획득 성공: key = {}", key);
			TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
			transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			return transactionTemplate.execute(status -> {
				try {
					return joinPoint.proceed();
				} catch (Throwable throwable) {
					status.setRollbackOnly();
					if (throwable instanceof RuntimeException) {
						throw (RuntimeException)throwable;
					} else if (throwable instanceof Error) {
						throw (Error)throwable;
					} else {
						throw new RuntimeException("트랜잭션 실행 중 오류 발생", throwable);
					}
				}
			});
		} finally {
			if (isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
				log.info("락 해제: key = {}", key);
			}
		}
	}
}
