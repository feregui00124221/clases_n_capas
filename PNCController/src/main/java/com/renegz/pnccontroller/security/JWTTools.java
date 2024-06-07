package com.renegz.pnccontroller.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
public class JWTTools {
    @Value("${spring.jwt.secret}")
    private static Key key;

//    @Value("${spring.jwt.exptime}")
//    private Integer exp;

    public String generateToken(Authentication authentication) {
//        Map<String, Object> claims = new HashMap<>();
//
//        return Jwts
//                .builder()
//                .claims(claims)
//                .subject(user.getEmail())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + exp))
//                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
//                .compact();
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        System.out.println("New token :");
        System.out.println(token);
        return token;
    }

    public Boolean verifyToken(String token) {
        try {
//            JwtParser parser = Jwts
//                    .parser()
//                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes()))
//                    .build();
//
//            parser.parse(token);
//            return true;
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect", e.fillInStackTrace());
        }
    }

    public String getUsernameFromToken(String token) {
//        try {
//            JwtParser parser = Jwts.parser()
//                    .verifyWith(Keys.hmacShaKeyFor(key.getBytes()))
//                    .build();
//
//            return parser.parseSignedClaims(token)
//                    .getPayload()
//                    .getSubject();
//        } catch (Exception e) {
//            return null;
//        }
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
