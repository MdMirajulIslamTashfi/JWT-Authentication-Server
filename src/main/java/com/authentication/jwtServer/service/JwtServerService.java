package com.authentication.jwtServer.service;

import com.authentication.jwtServer.dto.TokenRequestDto;
import com.authentication.jwtServer.dto.TokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
public class JwtServerService {

    private final String secret = "MySuperSecretKeyForJWTSigningThatIsLongEnough123!";
    private final Long expirationMs = 3600000L; // 1 hour in ms

    private SecretKey signInKey;

    @PostConstruct
    public void init() {
        this.signInKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Token Generation
    public TokenResponseDto generateToken(TokenRequestDto request) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        String token = Jwts.builder()
                .subject(request.getEmail())
                .claim("email", request.getEmail())
                .claim("password", request.getPassword())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signInKey)
                .compact();
        log.info("Generated Token for User Email = {}", request.getEmail());
        return new TokenResponseDto(token, expirationMs);
    }
}
