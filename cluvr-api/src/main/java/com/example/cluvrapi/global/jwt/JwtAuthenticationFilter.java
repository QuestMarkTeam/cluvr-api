package com.example.cluvrapi.global.jwt;//package com.example.cluvrapi.global.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.*;
//import java.io.IOException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req,
//        HttpServletResponse res,
//        FilterChain chain)
//        throws ServletException, IOException {
//        String header = req.getHeader("Authorization");
//        String token  = jwtUtil.resolveToken(header);
//
//        if (token != null && jwtUtil.validateToken(token)) {
//            // TODO: 실제 비즈니스 로직에 맞춰 Authentication 객체 생성
//            // 예: Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//            // SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        chain.doFilter(req, res);
//    }
//}
