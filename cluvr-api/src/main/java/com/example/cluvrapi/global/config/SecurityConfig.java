package com.example.cluvrapi.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.jwt.CustomUserDetailsService;
import com.example.cluvrapi.global.jwt.JwtAuthenticationFilter;
import com.example.cluvrapi.global.jwt.JwtUtil;
import com.example.cluvrapi.global.jwt.RefreshTokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	private final RefreshTokenService refreshTokenService;

	public SecurityConfig(
		CustomUserDetailsService customUserDetailsService,
		JwtUtil jwtUtil,
		UserRepository userRepository,
		RefreshTokenService refreshTokenService
	) {
		this.customUserDetailsService = customUserDetailsService;         // ← 추가
		this.jwtUtil = jwtUtil;                                           // ← 추가
		this.userRepository = userRepository;
		this.refreshTokenService = refreshTokenService; // ← 추가
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
	public SecurityFilterChain filterChain(HttpSecurity http) throws
		Exception {

		http
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.sessionManagement(sm ->
				sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.userDetailsService(customUserDetailsService)
			.authorizeHttpRequests(auth -> auth
				// 회원가입·로그인만 공개
				.requestMatchers("/auth/signup", "/auth/login").permitAll()
				// /admin/** 은 ADMIN 권한 필요
				.requestMatchers("/admin/**").hasRole("ADMIN")
				// 그 외 모든 요청은 인증된 사용자여야 함
				.anyRequest().authenticated()
			)
			.addFilterBefore(
				new JwtAuthenticationFilter(jwtUtil, customUserDetailsService, refreshTokenService),
				UsernamePasswordAuthenticationFilter.class
			);

		return http.build();
	}

}
