package com.example.cluvrnotifications.domain.notification.repository.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.cluvrnotifications.domain.notification.entity.NotificationDocument;
import com.example.cluvrnotifications.domain.notification.repository.custom.NotificationCacheRepositoryCustom;

@Repository
@RequiredArgsConstructor
public class NotificationCacheRepositoryImpl implements NotificationCacheRepositoryCustom {

	private final MongoTemplate mongoTemplate;

	@Override
	public List<NotificationDocument> findAllByReceiverId(Long receiverId) {
		Query query = new Query(Criteria.where("receiverId").is(receiverId));
		return mongoTemplate.find(query, NotificationDocument.class);
	}

	@Override
	public void deleteAllById(List<String> ids) {
		Query query = new Query(Criteria.where("_id").in(ids));
		mongoTemplate.remove(query, NotificationDocument.class);
	}
}


