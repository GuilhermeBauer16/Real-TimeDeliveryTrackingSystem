package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {

    public static final long JWT_TOKEN_VALIDITY = 180000L;


    private final String secret;

    public JwtTokenService(@Value("${SECRET_KEY}") String jwtSecret) {
        secret = jwtSecret;
    }

    private Claims getAllClaimsFromToken(String token) {
        final SecretKey secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token).getPayload();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return this.getClaimFromToken(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, String> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
        return this.getToken(claims, userDetails.getUsername());
    }

    private String getToken(Map<String, String> claims, String subject) {
        final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 100))
                .signWith(secretKey)
                .compact();


    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String usernameFromUserDetails = userDetails.getUsername();
        final String userFromJWT = this.getUsernameFromToken(token);

        return (userFromJWT.equals(usernameFromUserDetails) && !this.isTokenExpired(token));
    }

}
