package com.example.cluvrapi.domain.join.service.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 클럽 즉시가입 시 동시성 제어를 위한 분산 락 어노테이션
 *
 * userId 락을 획득할 유저 ID (SpEL 표현식 지원)
 * clubId 락을 획득할 클럽 ID (SpEL 표현식 지원)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectJoinLock {
	String userId();  // SpEL 형식: "#userId"
	String clubId();  // SpEL 형식: "#clubId"
}
