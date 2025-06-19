package com.example.cluvrapi.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.cluvrapi.global.jwt.CustomUserDetailsService;
import com.example.cluvrapi.global.jwt.JwtAuthenticationFilter;
import com.example.cluvrapi.global.jwt.JwtUtil;
import com.example.cluvrapi.global.jwt.RefreshTokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final JwtUtil jwtUtil;
	private final RefreshTokenService refreshTokenService;

	public SecurityConfig(
		CustomUserDetailsService userDetailsService,
		JwtUtil jwtUtil,
		RefreshTokenService refreshTokenService
	) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
		this.refreshTokenService = refreshTokenService;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration configuration
	) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	@Order(1)
	public SecurityFilterChain clubChain(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
		http
			.securityMatcher("/api/clubs/**")
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.userDetailsService(userDetailsService)
			.addFilterBefore(
				new JwtAuthenticationFilter(jwtUtil, userDetailsService, refreshTokenService),
				UsernamePasswordAuthenticationFilter.class
			)
			.authorizeHttpRequests(auth -> auth
				// OWNER 기능 (메타 애노테이션으로 실제 제어)
				.requestMatchers("/api/clubs/{clubId}/settings/**").authenticated()
				// ADMIN 기능
				.requestMatchers("/api/clubs/{clubId}/members/**").authenticated()
				// MEMBER 기능
				.requestMatchers("/api/clubs/{clubId}/**").authenticated()
				// 클럽 내/외 공통 ADMIN-only
				.requestMatchers("/admin/**").hasRole("ADMIN")
				// 나머지 클럽 URL: 인증만
				.anyRequest().authenticated()
			);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultChain(HttpSecurity http) throws
		Exception {

		http
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.sessionManagement(sm ->
				sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.userDetailsService(userDetailsService)
			.authorizeHttpRequests(auth -> auth
				// 회원가입·로그인만 공개
				.requestMatchers("/auth/signup", "/auth/login", "/my-monitor/**", "/**").permitAll()
				// /admin/** 은 ADMIN 권한 필요
				.requestMatchers("/admin/**").hasRole("ADMIN")
				// 그 외 모든 요청은 인증된 사용자여야 함
				.anyRequest().authenticated()
			)
			.addFilterBefore(
				new JwtAuthenticationFilter(jwtUtil, userDetailsService, refreshTokenService),
				UsernamePasswordAuthenticationFilter.class
			);

		return http.build();
	}

}
