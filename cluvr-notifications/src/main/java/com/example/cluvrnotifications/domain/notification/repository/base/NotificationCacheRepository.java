package com.example.cluvrnotifications.domain.notification.repository.base;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.cluvrnotifications.domain.notification.entity.NotificationDocument;
import com.example.cluvrnotifications.domain.notification.repository.custom.NotificationCacheRepositoryCustom;

public interface NotificationCacheRepository extends MongoRepository<NotificationDocument, String>,
	NotificationCacheRepositoryCustom {

	List<NotificationDocument> findByReceiverId(Long receiverId);
}
