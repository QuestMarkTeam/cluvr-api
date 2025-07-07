package com.example.cluvrapi.domain.join.service.lock;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
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
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();

		// SpEL 준비
		SpelExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i], args[i]);
		}

		// clubId 및 userId 추출
		String clubId = parser.parseExpression(directJoinLock.clubId()).getValue(context, String.class);
		String userId = parser.parseExpression(directJoinLock.userId()).getValue(context, String.class);
		if (clubId == null || userId == null) {
			throw new IllegalArgumentException("clubId 또는 userId 파라미터가 누락되었습니다.");
		}

		String lockKey = "lock:club-join:" + clubId;
		String cacheKey = "join-request:club:" + clubId + ":user:" + userId;

		RBucket<Boolean> bucket = redissonClient.getBucket(cacheKey);
		if (Boolean.TRUE.equals(bucket.get())) {
			log.warn("중복 요청 차단: {}", cacheKey);
			throw new IllegalStateException("이미 처리 중인 요청입니다.");
		}

		// 캐시 등록 (중복 방지)
		bucket.set(true, 6, TimeUnit.SECONDS);

		RLock lock = redissonClient.getLock(lockKey);
		boolean isLocked = false;

		try {
			isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
			if (!isLocked) {
				throw new IllegalStateException("락 획득 실패: key = " + lockKey);
			}

			log.info("락 획득 성공: key = {}", lockKey);

			TransactionTemplate tx = new TransactionTemplate(transactionManager);
			tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			return tx.execute(status -> {
				try {
					return joinPoint.proceed();
				} catch (Throwable t) {
					status.setRollbackOnly();
					throw new RuntimeException("트랜잭션 실행 중 오류 발생", t);
				}
			});
		} finally {
			if (isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
				log.info("락 해제: key = {}", lockKey);
			}
		}
	}

}
