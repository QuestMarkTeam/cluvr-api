// package com.example.cluvrapi.global.jwt;
//
// import java.io.IOException;
//
// import org.springframework.http.HttpHeaders;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.util.StringUtils;
// import org.springframework.web.filter.OncePerRequestFilter;
//
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
// 	private final JwtUtil jwtUtil;
// 	private final CustomUserDetailsService customUserDetailsService;
// 	private final RefreshTokenService refreshTokenService;
//
// 	@Override
// 	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
// 		FilterChain filterChain) throws ServletException, IOException {
//
// 		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
// 		if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
// 			filterChain.doFilter(request, response);
// 			return;
// 		}
// 		String token = header.substring(7);
//
// 		if (refreshTokenService.isAccessTokenBlacklisted(token)) {
// 			// 이미 블랙리스트에 있으면 인증 없이 바로 통과(=차단)
// 			filterChain.doFilter(request, response);
// 			return;
// 		}
//
// 		if (!jwtUtil.validateToken(token)) {
// 			filterChain.doFilter(request, response);
// 			return;
// 		}
//
// 		Long userId = jwtUtil.getUserIdFromToken(token);
//
// 		CustomUserDetails userDetails = customUserDetailsService.loadUserById(userId);
// 		if (userDetails != null) {
// 			UsernamePasswordAuthenticationToken authentication =
// 				new UsernamePasswordAuthenticationToken(
// 					userDetails,
// 					null,
// 					userDetails.getAuthorities()
// 				);
// 			SecurityContextHolder.getContext().setAuthentication(authentication);
// 		}
//
// 		filterChain.doFilter(request, response);
// 	}
// }
