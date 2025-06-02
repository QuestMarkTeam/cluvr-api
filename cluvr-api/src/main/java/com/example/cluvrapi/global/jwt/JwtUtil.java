package com.example.cluvrapi.global.jwt;//package com.example.cluvrapi.global.jwt;
//
//import io.jsonwebtoken.*;
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//import javax.crypto.spec.SecretKeySpec;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//@Component
//public class JwtUtil {
//
//    private static final String BEARER = "Bearer ";
//    private static final long ACCESS_EXP_MS  = 1000L * 60 * 60 * 2;      // 2시간
//    private static final long REFRESH_EXP_MS = 1000L * 60 * 60 * 24 * 7; // 1주일
//
//    @Value("${jwt.secret.key}")
//    private String secretKey;
//
//    private Key getSigningKey() {
//        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
//        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
//    }
//
//    // 헤더에서 "Bearer " 제거
//    public String resolveToken(String header) {
//        if (StringUtils.hasText(header) && header.startsWith(BEARER)) {
//            return header.substring(BEARER.length());
//        }
//        return null;
//    }
//
//    // 서명·만료 검사
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//
//    // subject(userId) 꺼내기
//    public String getSubject(String token) {
//        return Jwts.parserBuilder()
//            .setSigningKey(getSigningKey())
//            .build()
//            .parseClaimsJws(token)
//            .getBody()
//            .getSubject();
//    }
//
//    // AccessToken 발급
//    public String generateAccessToken(String subject) {
//        return BEARER + Jwts.builder()
//            .setSubject(subject)
//            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP_MS))
//            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//            .compact();
//    }
//
//    // RefreshToken 발급
//    public String generateRefreshToken(String subject) {
//        return BEARER + Jwts.builder()
//            .setSubject(subject)
//            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP_MS))
//            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//            .compact();
//    }
//}
