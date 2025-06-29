package com.example.cluvrapi.domain.user.repository;

import java.util.Optional;

import com.example.cluvrapi.domain.common.repository.BaseRepository;
import com.example.cluvrapi.domain.user.entity.User;

public interface UserRepository extends BaseRepository<User, Long>, UserRepositoryCustom {
	Optional<User> findByEmail(String email);
}
