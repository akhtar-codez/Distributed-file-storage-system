package com.distributedstorage.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Secret key injected from application.properties — never hardcoded
    @Value("${jwt.secret}")
    private String secret;

    // Token expiration injected from application.properties
    @Value("${jwt.expiration}")
    private long expirationMs;

    // Generates a SecretKey object from the secret string
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generates a JWT token for the given email and numeric userId
    // Token contains: subject (email), userId claim, issued time, expiry time
    public String generateToken(String email, Long userId) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)  // embed numeric DB id for use in frontend/upload
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    // Extracts the email (subject) from a JWT token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Extracts the numeric userId from the token claims
    public Long extractUserId(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    // Checks if the token is still valid (not expired)
    public boolean isTokenValid(String token) {
        try {
            return getClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            // Token is invalid or tampered
            return false;
        }
    }

    // Parses and returns all claims from the token
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}