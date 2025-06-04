package com.example.cluvrapi.global.jwt;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final JwtUtil jwtUtil;
	private final StringRedisTemplate redisTemplate;

	private static final String REDIS_KEY_PREFIX = "refreshToken:";

	@Override
	public String createRefreshToken(Long userId, String role) {
		// 1) JwtUtil로 새로운 리프레시 토큰 발급
		String refreshToken = jwtUtil.generateRefreshToken(userId, role);

		// 2) Redis에 저장할 키: "refreshToken:{userId}"
		String redisKey = REDIS_KEY_PREFIX + userId;

		// 3) Redis에 토큰 저장 (만료 시간은 JwtUtil의 REFRESH_TOKEN_EXPIRATION_MS 와 동일하게 설정)
		redisTemplate.opsForValue()
			.set(redisKey, refreshToken, jwtUtil.REFRESH_TOKEN_EXPIRATION_MS, TimeUnit.MILLISECONDS);

		return refreshToken;
	}

	@Override
	public boolean validateRefreshToken(String token) {
		// 1) 토큰 자체의 서명·만료 검사
		if (!jwtUtil.validateToken(token)) {
			return false;
		}

		// 2) 토큰에서 userId 추출
		Long userId = jwtUtil.getUserIdFromToken(token);

		// 3) Redis에서 userId로 저장된 토큰 조회
		String redisKey = REDIS_KEY_PREFIX + userId;
		String storedToken = redisTemplate.opsForValue().get(redisKey);

		// 4) Redis에 저장된 토큰이 존재하고, 전달받은 토큰과 일치하는지 비교
		return token.equals(storedToken);
	}

	@Override
	public void deleteRefreshToken(Long userId) {
		String redisKey = REDIS_KEY_PREFIX + userId;
		redisTemplate.delete(redisKey);
	}
}
