package com.example.cluvrnotifications.domain.notification.dto.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;

import com.example.cluvrnotifications.domain.notification.entity.NotificationSetting;

@Getter
public class UpdateNotificationsSettingResponseDto {
	private final Map<String, Boolean> settings;

	public UpdateNotificationsSettingResponseDto(Map<String, Boolean> settings) {
		this.settings = settings;
	}

	public static UpdateNotificationsSettingResponseDto from(List<NotificationSetting> settingList) {
		Map<String, Boolean> result = settingList.stream()
			.collect(Collectors.toMap(
				s -> s.getNotificationType().name(),
				NotificationSetting::getIsEnabled
			));
		return new UpdateNotificationsSettingResponseDto(result);
	}
}
