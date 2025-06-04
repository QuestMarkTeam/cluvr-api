package com.example.cluvrapi.global.jwt;

public interface RefreshTokenService {
	/**
	 * 리프레시 토큰 생성 및 저장
	 * @param userId 사용자 ID (subject)
	 * @param role   사용자 권한
	 * @return 발급된 리프레시 토큰 문자열
	 */
	String createRefreshToken(Long userId, String role);

	/**
	 * 리프레시 토큰 검증
	 * @param token 확인할 토큰
	 * @return 유효하면 true, 아니면 false
	 */
	boolean validateRefreshToken(String token);

	/**
	 * 리프레시 토큰 삭제 (로그아웃 시 등)
	 * @param userId 사용자 ID
	 */
	void deleteRefreshToken(Long userId);
}
