package com.example.cluvrnotifications.domain.notification.repository.base;

import com.example.cluvrnotifications.common.repository.BaseRepository;
import com.example.cluvrnotifications.domain.notification.entity.Notification;
import com.example.cluvrnotifications.domain.notification.repository.custom.NotificationRepositoryCustom;
import com.example.cluvrnotifications.domain.user.entity.User;

public interface NotificationRepository extends BaseRepository<Notification, Long>, NotificationRepositoryCustom {

	Long user(User user);
}
