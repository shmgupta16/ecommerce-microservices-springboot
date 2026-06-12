package com.example.ecommerce.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final String issuer;
    private final long expirationMinutes;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    public String issue(String subject) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .sign(algorithm);
    }

    public Optional<DecodedJWT> verify(String token) {
        try {
            return Optional.of(verifier.verify(token));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }
}
