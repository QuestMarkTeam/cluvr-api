package com.example.cluvrapi.global.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.cluvrapi.global.resolver.AuthenticationArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final AuthenticationArgumentResolver authArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authArgumentResolver);
	}

	// 정적 이미지
	@Override
	public void  addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/img/**")
			.addResourceLocations("classpath:/static/img/");
	}
}