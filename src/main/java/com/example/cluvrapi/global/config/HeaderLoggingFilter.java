package com.example.cluvrapi.global.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class HeaderLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain)
		throws ServletException, IOException {

		// System.out.println("🔍 Authorization Header = " + request.getHeader("Authorization"));
		// System.out.println("🔍 Request URI = " + request.getRequestURI());
		filterChain.doFilter(request, response);
	}
}
