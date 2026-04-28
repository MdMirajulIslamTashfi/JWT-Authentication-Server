package com.authentication.jwtServer.service;

import com.authentication.jwtServer.dto.TokenRequestDto;
import com.authentication.jwtServer.dto.TokenResponseDto;
import com.authentication.jwtServer.dto.ValidateTokenRequestDto;
import com.authentication.jwtServer.dto.ValidateTokenResponseDto;
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
                .claim("role", request.getRole())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signInKey)
                .compact();
        log.info("Generated Token for User Email = {}", request.getEmail());
        return new TokenResponseDto(token, expirationMs);
    }

    // Token Validation

    public ValidateTokenResponseDto validateToken(ValidateTokenRequestDto request) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signInKey)
                    .build()
                    .parseSignedClaims(request.getToken())
                    .getPayload();

            String email  = claims.get("email", String.class);
            String role = claims.get("role", String.class);

            log.info("Token valid for email={}", email);
            return new ValidateTokenResponseDto(true, "OK", email, role);

        } catch (ExpiredJwtException ex) {
            log.warn("Token expired: {}", ex.getMessage());
            return new ValidateTokenResponseDto(false, "Token has expired", null, null);

        } catch (MalformedJwtException ex) {
            log.warn("Malformed token: {}", ex.getMessage());
            return new ValidateTokenResponseDto(false, "Token is malformed", null, null);

        } catch (SecurityException ex) {
            log.warn("Invalid signature: {}", ex.getMessage());
            return new ValidateTokenResponseDto(false, "Invalid token signature", null, null);

        } catch (Exception ex) {
            log.warn("Token validation failed: {}", ex.getMessage());
            return new ValidateTokenResponseDto(false, "Token validation failed", null, null);
        }
    }
}
