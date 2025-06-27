package com.example.cluvrapi.global.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
	public SecurityFilterChain clubChain(HttpSecurity http,
		CustomUserDetailsService userDetailsService) throws Exception {
		http
			.securityMatcher("/api/clubs/**")  // 이 체인은 이 경로에만 적용
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
				.anyRequest().authenticated()
			);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultChain(HttpSecurity http) throws
		Exception {

		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.sessionManagement(sm ->
				sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.userDetailsService(userDetailsService)
			.authorizeHttpRequests(auth -> auth
				// 회원가입·로그인만 공개
				.requestMatchers("/api/auth/signup", "/api/auth/login", "/api/auth/verify", "/my-monitor/**", "/**",  "/favicon.ico").permitAll()
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

	@Value("${app.cors.allowed-origins}")
	private List<String> allowedOrigins;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		System.out.println(allowedOrigins);
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(allowedOrigins);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
