package com.example.cluvrnotifications.domain.notification.repository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.cluvrnotifications.domain.notification.dto.response.QReadNotificationResponseDto;
import com.example.cluvrnotifications.domain.notification.dto.response.ReadNotificationResponseDto;
import com.example.cluvrnotifications.domain.notification.entity.Notification;
import com.example.cluvrnotifications.domain.notification.entity.NotificationSetting;
import com.example.cluvrnotifications.domain.notification.entity.QNotification;
import com.example.cluvrnotifications.domain.notification.entity.QNotificationSetting;
import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * QueryDSL을 이용한 Notification 관련 복잡한 조회 쿼리 구현체
 *
 * - Spring Data JPA의 네이밍 방식으로 표현하기 어려운 조건을 커버
 * - Custom Repository와 연결됨 (NotificationRepositoryCustom)
 *
 *
 * @author 정은세
 */

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	/**
	 * 설명: 특정 사용자(userId)의 알림 목록을 페이징으로 조회함
	 *
	 * - isRead 값이 null일 경우 전체 알림 조회
	 * - isRead 값이 true/false일 경우 읽음/안 읽음 필터링 적용
	 *
	 * @param userId {사용자 식별자}
	 * @param isRead {읽음 여부 (null일 경우 전체)}
	 * @param pageable {페이지 및 정렬 정보}
	 * @return {페이징된 알림 목록}
	 *
	 * @author 정은세
	 */
	@Override
	public Page<ReadNotificationResponseDto> findAllDtosByUserId(Long userId, Boolean isRead, Pageable pageable) {
		QNotification notification = QNotification.notification;

		List<ReadNotificationResponseDto> content = queryFactory
			.select(new QReadNotificationResponseDto(
				notification.id,
				notification.content,
				notification.isRead
			))
			.from(notification)
			.where(
				notification.user.id.eq(userId),
				isRead != null ? notification.isRead.eq(isRead) : null
			)
			.orderBy(notification.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long count = queryFactory
			.select(notification.count())
			.from(notification)
			.where(
				notification.user.id.eq(userId),
				isRead != null ? notification.isRead.eq(isRead) : null
			)
			.fetchOne();

		return new PageImpl<>(content, pageable, count != null ? count : 0);
	}

	/**
	 * 설명: 알림 ID와 사용자 ID로 특정 알림 단건을 조회
	 *
	 * - 보안 상 다른 사용자의 알림 접근을 막기 위한 이중 조건
	 *
	 * @param id {알림 식별자}
	 * @param userId {사용자 식별자}
	 * @return Optional<Notification> {해당 조건에 맞는 알림 (없을 경우 빈 Optional)}
	 *
	 * @author 정은세
	 */

	@Override
	public Optional<Notification> findByIdAndUserId(Long id, Long userId) {
		QNotification notification = QNotification.notification;

		Notification result = queryFactory
			.selectFrom(notification)
			.where(
				notification.id.eq(id),
				notification.user.id.eq(userId)
			)
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public List<NotificationSetting> findAllByUserId(Long userId) {
		QNotificationSetting setting = QNotificationSetting.notificationSetting;

		return queryFactory
			.selectFrom(setting)
			.where(setting.user.id.eq(userId))
			.fetch();
	}
}
