//package com.example.otwAppservice.Security;
//
//import io.jsonwebtoken.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import java.util.Date;
//
//@Component
//public class JwtTokenUtil {
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;
//
//    // Generate JWT token
//    public String generateToken(String username) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + expiration);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }
//
//    // Validate JWT token
//    public boolean validateToken(String token, String username) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            String tokenUsername = claims.getSubject();
//            return (tokenUsername.equals(username) && !isTokenExpired(token));
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // Extract username from JWT token
//    public String extractUsername(String token) {
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            return claims.getSubject();
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    // Check if JWT token is expired
//    private boolean isTokenExpired(String token) {
//        Date expiryDate = Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration();
//        return expiryDate.before(new Date());
//    }
//}
