package com.example.cluvrapi.global.client;

import org.hibernate.query.derived.AnonymousTupleBasicEntityIdentifierMapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.cluvrapi.domain.reaction.dto.request.QueueInitRequestDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationQueueClient {

	private final RestTemplate restTemplate;

	@Value("${noti.server.url}")
	private String notiServerUrl;

	public void initQueueIfNecessary(Long userId){
		String url = notiServerUrl + "/notifications/queue/init";

		QueueInitRequestDto dto = new QueueInitRequestDto(userId);
		restTemplate.postForEntity(url, dto, Void.class);
	}
}
