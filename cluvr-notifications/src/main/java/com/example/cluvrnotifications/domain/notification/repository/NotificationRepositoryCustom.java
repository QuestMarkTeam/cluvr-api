package com.example.cluvrnotifications.domain.notification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.cluvrnotifications.domain.notification.dto.response.ReadNotificationResponseDto;
import com.example.cluvrnotifications.domain.notification.entity.Notification;
import com.example.cluvrnotifications.domain.notification.entity.NotificationSetting;

public interface NotificationRepositoryCustom {

	Page<ReadNotificationResponseDto> findAllDtosByUserId(Long userId, Boolean isRead, Pageable pageable);

	Optional<Notification> findByIdAndUserId(Long id, Long userId);

	List<NotificationSetting> findAllByUserId(Long userId);

}

