package com.auth_service.auth_service.service;

import com.auth_service.auth_service.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.management.JMException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    private final SecretKey secretKey;
    private final UserService userService;

    public JwtService(@Value("${jwt.secret}") String secretKey, UserService userService){
        byte[] bytes = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        this.secretKey = Keys.hmacShaKeyFor(bytes);
        this.userService = userService;
    }


    public String generateToken(String email) {

        Map<String, Object> claims = new HashMap<>();
        Date expireTime = new Date(System.currentTimeMillis() + 60 * 1000 * 30);
        return createToken(email, claims, expireTime);
    }


    public String generateToken(String email, Map<String, Object> claims) {

        Date expireTime = new Date(System.currentTimeMillis() + 60 * 1000 * 30);
        return createToken(email, claims, expireTime);
    }


    public String generateToken(String email, Map<String, Object> claims, Date expireTime) {
        return createToken(email, claims, expireTime);
    }


    private String createToken(String email, Map<String, Object> claims, Date expireTime) {
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expireTime)
                .and()
                .signWith(secretKey)
                .compact();
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAlLClaims(token);
        return claimResolver.apply(claims);
    }


    public Claims extractAlLClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build().parseSignedClaims(token).getPayload();
    }

    public boolean validateToken(String token) {
        try {
            // extract all claims  from the token
            String email = this.extractUserEmail(token);
            User user = userService.findUserByEmail(email).orElse(null);
            return user != null && !isTokenExpired(token);
        } catch (JwtException ex) {
            throw new JwtException("Invalid token");
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return  extractClaim(token, Claims::getExpiration);
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
