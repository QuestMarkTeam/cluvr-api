package com.example.cluvrapi.global.jwt;

public interface RefreshTokenService {

	String createRefreshToken(Long userId, String role);

	boolean validateRefreshToken(String token);

	void saveRefreshToken(Long userId, String refreshToken);

	void deleteRefreshToken(Long userId);

	void blacklistAccessToken(String accessToken, long remainingMillis);

	boolean isAccessTokenBlacklisted(String accessToken);
}
