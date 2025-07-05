// package com.example.cluvrapi.domain.notification.event;
//
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
//
// import com.example.cluvrapi.domain.notification.enums.NotiTargetType;
// import com.example.cluvrapi.domain.notification.enums.NotificationType;
//
// @SpringBootTest
// class NotificationProducerTest {
//
// 	@Autowired
// 	private NotificationProducer notificationProducer;
//
// 	@Test
// 	void 실제_MQ에_라우팅키_기반_전송이_된다() {
// 		NotificationEvent event = NotificationEvent.from(
// 			9999L, NotificationType.COMMENT, "라우팅 테스트", NotiTargetType.USER, 100L);
//
// 		notificationProducer.send(event);
//
// 		// 직접 확인은 RabbitMQ 대시보드 or rabbitTemplate.receiveAndConvert("user.1")
// 		// 또는 로그 찍어서도 확인 가능
// 	}
// }
