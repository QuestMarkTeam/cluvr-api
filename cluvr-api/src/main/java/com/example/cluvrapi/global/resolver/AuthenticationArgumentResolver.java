package com.example.cluvrapi.global.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.global.jwt.CustomUserDetails;
import com.example.cluvrapi.global.response.ResponseCode;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Auth.class)
			&& AuthUser.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new ResponseStatusException(
				ResponseCode.AUTH_REQUIRED.getStatus(),
				ResponseCode.AUTH_REQUIRED.getDefaultMessage()
			);
		}

		Object principal = authentication.getPrincipal();
		if (!(principal instanceof CustomUserDetails)) {
			throw new ResponseStatusException(
				ResponseCode.TOKEN_INVALID.getStatus(),
				ResponseCode.TOKEN_INVALID.getDefaultMessage()
			);
		}

		CustomUserDetails userDetails = (CustomUserDetails)principal;
		return new AuthUser(
			userDetails.getUser().getId(),
			userDetails.getUser().getEmail()
		);
	}
}