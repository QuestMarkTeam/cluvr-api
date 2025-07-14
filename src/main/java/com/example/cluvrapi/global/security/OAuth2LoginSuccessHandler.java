package com.example.cluvrapi.global.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.cluvrapi.domain.auth.dto.response.SocialLoginResponseDto;
import com.example.cluvrapi.domain.auth.service.AuthService;
import com.example.cluvrapi.global.response.BaseResponse;
import com.example.cluvrapi.global.response.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	private final AuthService authService;
	private final ObjectMapper objectMapper;

	public OAuth2LoginSuccessHandler(AuthService authService, ObjectMapper objectMapper) {
		this.authService   = authService;
		this.objectMapper  = objectMapper;
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		// Spring Security가 얻어온 OIDC 사용자 정보
		OidcUser oidcUser = (OidcUser) ((OAuth2AuthenticationToken) authentication).getPrincipal();
		// id_token 바로 꺼내기
		String idToken = oidcUser.getIdToken().getTokenValue();

		// 비즈니스 로직 실행 (소셜 로그인·연동)
		// SocialLoginResponseDto loginResp = authService.socialLogin(idToken);

		// JSON 응답
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// objectMapper.writeValue(response.getWriter(),
		// 	BaseResponse.success(loginResp, ResponseCode.OK)
		// );
	}
}
