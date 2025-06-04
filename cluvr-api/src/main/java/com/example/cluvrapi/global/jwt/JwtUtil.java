package com.example.cluvrapi.global.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * JWT 생성 · 파싱 · 검증 유틸리티 클래스
 */
@Component
public class JwtUtil {

	// 액세스 토큰 만료 시간 (예: 2시간)
	public final long ACCESS_TOKEN_EXPIRATION_MS = 1000L * 60 * 60 * 2;
	// 리프레시 토큰 만료 시간 (예: 7일)
	public final long REFRESH_TOKEN_EXPIRATION_MS = 1000L * 60 * 60 * 24 * 7;
	// application.yml 등에서 Base64로 인코딩된 시크릿 키를 주입받습니다.
	@Value("${jwt.secret.key}")
	private String secretKey;

	/**
	 * 액세스 토큰 생성
	 * @param userId 사용자 ID (subject)
	 * @param role   사용자 권한(예: "USER", "ADMIN")
	 * @return 발급된 JWT 액세스 토큰 문자열
	 */
	public String generateAccessToken(Long userId, String role) {
		return generateToken(userId, role, ACCESS_TOKEN_EXPIRATION_MS);
	}

	/**
	 * 리프레시 토큰 생성
	 * @param userId 사용자 ID (subject)
	 * @param role   사용자 권한
	 * @return 발급된 JWT 리프레시 토큰 문자열
	 */
	public String generateRefreshToken(Long userId, String role) {
		return generateToken(userId, role, REFRESH_TOKEN_EXPIRATION_MS);
	}

	private String generateToken(Long userId, String role, long expirationMillis) {
		// 1) 시크릿 키(Base64)를 디코딩하여 HMAC-SHA 키 객체로 변환
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		// 2) Claims 설정 (subject: userId, custom claim: role)
		Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
		claims.put("role", role);

		// 3) 발급 시각(now)과 만료 시각 설정
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationMillis);

		// 4) JWT 빌드 및 서명
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	/**
	 * 토큰에서 사용자 ID(subject) 추출
	 */
	public Long getUserIdFromToken(String token) {
		return Long.valueOf(parseToken(token).getSubject());
	}

	/**
	 * 토큰에서 role 클레임 추출
	 */
	public String getUserRoleFromToken(String token) {
		return parseToken(token).get("role", String.class);
	}

	/**
	 * 토큰 유효성(서명 + 만료) 검증
	 */
	public boolean validateToken(String token) {
		try {
			parseToken(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * 내부 헬퍼: JWT 파싱하여 Claims 반환 (예외 발생 시 JWTException)
	 */
	private Claims parseToken(String token) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}
}
