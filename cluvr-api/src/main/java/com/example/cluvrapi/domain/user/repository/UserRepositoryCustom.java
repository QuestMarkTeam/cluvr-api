package com.example.cluvrapi.domain.user.repository;

import java.util.Optional;

import com.example.cluvrapi.domain.user.entity.User;

public interface UserRepositoryCustom {
	Optional<User> findByEmail(String email);

	Optional<Long> findPointById(Long userId);
}
