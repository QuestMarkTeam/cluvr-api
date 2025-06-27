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
import org.springframework.http.HttpMethod;

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

	private final CorsProperties corsProperties;

	public SecurityConfig(CustomUserDetailsService userDetailsService,
		JwtUtil jwtUtil,
		RefreshTokenService refreshTokenService,
		CorsProperties corsProperties) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
		this.refreshTokenService = refreshTokenService;
		this.corsProperties = corsProperties;
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
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/api/auth/signup", "/api/auth/login", "/api/auth/verify", "/my-monitor/**",  "/favicon.ico", "/api/auth/test-signup").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			)
			.addFilterBefore(
				new JwtAuthenticationFilter(jwtUtil, userDetailsService, refreshTokenService),
				UsernamePasswordAuthenticationFilter.class
			);

		return http.build();
	}



	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		System.out.println(corsProperties);
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(corsProperties.getAllowedOrigins());
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
