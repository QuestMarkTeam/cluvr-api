package com.example.cluvrapi.global.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.cluvrapi.global.security.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtDecoder jwtDecoder;
	private final CorsProperties corsProperties;

	public SecurityConfig(
		JwtDecoder jwtDecoder,
		CorsProperties corsProperties
	) {
		this.jwtDecoder = jwtDecoder;
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
	public SecurityFilterChain clubChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/api/clubs/**")  // 이 체인은 이 경로에만 적용
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// Cognito JWT 사용
			.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.decoder(jwtDecoder))
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.anyRequest().authenticated()
			);

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultChain(HttpSecurity http, OAuth2LoginSuccessHandler OAuth2LoginSuccessHandler) throws
		Exception {

		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())
			.sessionManagement(sm ->
				sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(auth -> auth
				// 회원가입·로그인만 공개
				.requestMatchers("/api/auth/**", "/my-monitor/**", "/api/users/sub/**").permitAll()

				// /admin/** 은 ADMIN 권한 필요
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers("/api/auth/signup", "/api/auth/login", "/api/auth/verify", "/my-monitor/**",
					"/favicon.ico", "/api/auth/test-signup", "/api/auth/social-login", "/api/auth/complete-profile").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
			).oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.decoder(jwtDecoder))
			)        .oauth2Login(oauth2 -> oauth2
				.loginPage("/oauth2/authorization/cognito")
				.userInfoEndpoint(userInfo -> userInfo.oidcUserService(new OidcUserService()))
				.successHandler(OAuth2LoginSuccessHandler)
			);;

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
	// 정적파일 시큐리티 무시
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(
			"/index.html", "/", "/favicon.ico",
			"/css/**", "/js/**", "/images/**", "/payment/**"
		);
	}

}
