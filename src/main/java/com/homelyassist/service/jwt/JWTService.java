package com.homelyassist.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String key) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);

        return Jwts.builder()
                .setSubject(key)
                .setExpiration(Date.from(expirationTime.atZone(java.time.ZoneId.systemDefault()).toInstant()))
                .signWith(Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }
}
