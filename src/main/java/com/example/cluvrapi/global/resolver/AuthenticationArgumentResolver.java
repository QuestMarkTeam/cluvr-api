package com.example.cluvrapi.global.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.example.cluvrapi.domain.common.annotation.Auth;
import com.example.cluvrapi.domain.common.dto.AuthUser;
import com.example.cluvrapi.domain.user.entity.User;
import com.example.cluvrapi.domain.user.repository.UserRepository;
import com.example.cluvrapi.global.exception.BusinessException;
import com.example.cluvrapi.global.jwt.CustomUserDetails;
import com.example.cluvrapi.global.response.ResponseCode;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

	private final UserRepository userRepository;

	public AuthenticationArgumentResolver(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

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
		// if (!(principal instanceof Jwt jwt)) {
		// 	throw new ResponseStatusException(
		// 		ResponseCode.TOKEN_INVALID.getStatus(),
		// 		ResponseCode.TOKEN_INVALID.getDefaultMessage()
		// 	);
		// }
		//
		// String email = jwt.getClaim("email"); // claim 없으면 null
		//
		// // sub -> 사용자 ID, email은 클레임에 따라 없을 수도 있음
		// String sub = jwt.getSubject();
		// User user = userRepository.findBySub(sub)
		// 	.orElseThrow(() -> new BusinessException(ResponseCode.USER_NOT_FOUND));
		//
		// Long userId = user.getId();
		// if (userId == null) {
		// 	throw new ResponseStatusException(
		// 		ResponseCode.TOKEN_INVALID.getStatus(),
		// 		ResponseCode.TOKEN_BLACKLISTED.getDefaultMessage()
		// 	);
		// }

		// return new AuthUser(userId, email);

		if (!(principal instanceof CustomUserDetails userDetails)) {
			throw new ResponseStatusException(
				ResponseCode.TOKEN_INVALID.getStatus(),
				ResponseCode.TOKEN_INVALID.getDefaultMessage()
			);
		}
		Long userId = userDetails.getId();
		String email = userDetails.getUsername();
		return new AuthUser(userId, email);
	}
}
