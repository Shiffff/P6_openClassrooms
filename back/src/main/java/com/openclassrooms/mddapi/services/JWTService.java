package com.openclassrooms.mddapi.services;


import com.openclassrooms.mddapi.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import java.util.function.Function;

@Service
@Slf4j
@AllArgsConstructor
public class JWTService {
    private final String ENCRIPTION_KEY = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9O8YgYdD1YxficWfO06nDsmYgDdXmgMM4CN3bLor5c";
    private UserService userService;

    public Map<String, String> generate(String username) {
        User user = this.userService.loadUserByUsername(username);
        return this.generateJwt(user);
    }


    private Map<String, String> generateJwt(User user) {
        final long currentTime = System.currentTimeMillis();
        final long expirationTime = currentTime + 30 * 60 * 10000;

        final Map<String, Object> claims = Map.of(
                "nom", user.getUsername(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, user.getEmail()
        );


        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(user.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of("token", bearer);
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }


    public String extractUsername(String token) {
        return this.getClaim(token, Claims::getSubject);
    }


    private <T> T getClaim(String token, Function<Claims, T> function) {
        Claims claims = getAllClaims(token);
        return function.apply(claims);
    }

    private Claims getAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(this.getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT: {}", e.getMessage());
            throw e;
        }

    }
}