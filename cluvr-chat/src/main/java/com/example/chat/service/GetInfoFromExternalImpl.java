package com.example.chat.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.chat.dto.response.UserInfoResponseDto;
import com.example.global.response.response.BaseResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetInfoFromExternalImpl implements GetInfoFromExternal {
	private final RestTemplate restTemplate;

	// 유저별 가입한 클럽 리스트랑, 해당 클럽 리스트에서의 role정보
	@Override
	public UserInfoResponseDto getUserInfo(Long userId) {
		String url = "http://localhost:8080/api/users/" + userId;
		ResponseEntity<BaseResponse<UserInfoResponseDto>> response = restTemplate.exchange(
			url,
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<>() {
			}
		);
		return response.getBody().getData();
	}
}
