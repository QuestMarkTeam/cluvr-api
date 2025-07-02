package com.example.cluvrapi.domain.user.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.response.ResponseCode;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

	private final UserRepository userRepository;

	@Override
	public Long getUserIdBySub(String sub) {
		return userRepository.findBySub(sub)
			.map(User::getId)
			.orElseThrow(() -> new BusinessException(ResponseCode.USER_NOT_FOUND));

	}
}
