package com.example.cluvrapi.global.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 매 요청마다 Authorization 헤더에서 "Bearer {accessToken}"을 추출하고,
 * JwtUtil.validateToken()으로 유효성 검사 → DB에서 User를 조회하여
 * SecurityContextHolder에 UsernamePasswordAuthenticationToken을 심어주는 필터
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		// 1) “Authorization” 헤더 확인
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// 2) “Bearer ” 접두어 제거
		String token = header.substring(7);
		if (!jwtUtil.validateToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 3) 토큰이 유효하면, 클레임에서 userId와 role 추출
		Long userId = jwtUtil.getUserIdFromToken(token);
		String role = jwtUtil.getUserRoleFromToken(token);

		// 4) DB에서 User 조회
		User user = userRepository.findById(userId).orElse(null);
		if (user != null) {
			// 5) UsernamePasswordAuthenticationToken 생성 (principal: User, credentials: null, authorities: ROLE_xxx)
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
				java.util.List.of(new SimpleGrantedAuthority("ROLE_" + role)));
			// 6) SecurityContextHolder에 인증 정보 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		// 7) 나머지 필터 체인 이어가기
		filterChain.doFilter(request, response);
	}
}
