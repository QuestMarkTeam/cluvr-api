package com.example.cluvrapi.domain.user.repository;

import java.util.List;
import java.util.Optional;

import com.example.cluvrapi.domain.user.entity.User;

public interface UserRepositoryCustom {
	Optional<User> findByEmailAndNotDeleted(String email);

	Optional<Integer> findGemByIdNotDeleted(Long userId);

	Optional<User> findByIdNotDeleted(Long userId);

	List<User> findAllNotDeleted();

	boolean existsByEmail(String email);
	
	boolean existsByPhoneNumber(String phoneNumber);
}
