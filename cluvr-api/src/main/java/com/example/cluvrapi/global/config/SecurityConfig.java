package com.example.cluvrapi.global.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configurable
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf((auth) -> auth.disable());

		http.formLogin((auth) -> auth.disable());

		http.httpBasic((auth) -> auth.disable());

		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/login", "/", "/signin")
			.permitAll()
			.requestMatchers("/admin")
			.hasRole("ADMIN")
			.anyRequest()
			.authenticated());

		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();

	}
}
