package com.example.cluvrapi.global.jwt;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmailAndNotDeleted(email);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("등록된 사용자가 없습니다. email=" + email);
		}
		User user = optionalUser.get();
		return new CustomUserDetails(user);
	}

	public CustomUserDetails loadUserById(Long userId) {
		Optional<User> optionalUser = userRepository.findByIdNotDeleted(userId);
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("등록된 사용자가 없습니다. id=" + userId);
		}
		User user = optionalUser.get();
		return new CustomUserDetails(user);
	}
}
