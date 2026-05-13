package com.example.movies_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class JwtServiceTest {

    @Test
    void generateToken_shouldCreateSignedJwtWithCustomerClaims() throws Exception {
        JwtService jwtService = new JwtService(
                "test-secret-with-enough-length-for-hmac-signing",
                3600);

        String token = jwtService.generateToken("valid@example.com", 42);

        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);

        String header = decode(parts[0]);
        String payload = decode(parts[1]);

        assertEquals("{\"alg\":\"HS256\",\"typ\":\"JWT\"}", header);
        assertTrue(payload.contains("\"sub\":\"valid@example.com\""));
        assertTrue(payload.contains("\"email\":\"valid@example.com\""));
        assertTrue(payload.contains("\"customerId\":42"));

        Long issuedAt = numericClaim(payload, "iat");
        Long expiresAt = numericClaim(payload, "exp");
        assertNotNull(issuedAt);
        assertNotNull(expiresAt);
        assertTrue(expiresAt > issuedAt);
    }

    @Test
    void validateToken_shouldReturnClaimsForValidToken() {
        JwtService jwtService = new JwtService(
                "test-secret-with-enough-length-for-hmac-signing",
                3600);

        String token = jwtService.generateToken("valid@example.com", 42);

        Optional<JwtService.JwtClaims> claims = jwtService.validateToken(token);

        assertTrue(claims.isPresent());
        assertEquals("valid@example.com", claims.get().email());
        assertEquals(42, claims.get().customerId());
    }

    @Test
    void validateToken_shouldRejectInvalidSignature() {
        JwtService jwtService = new JwtService(
                "test-secret-with-enough-length-for-hmac-signing",
                3600);

        String token = jwtService.generateToken("valid@example.com", 42) + "tampered";

        assertFalse(jwtService.isTokenValid(token));
    }

    @Test
    void validateToken_shouldRejectExpiredToken() {
        JwtService jwtService = new JwtService(
                "test-secret-with-enough-length-for-hmac-signing",
                -1);

        String token = jwtService.generateToken("valid@example.com", 42);

        assertFalse(jwtService.isTokenValid(token));
    }

    private String decode(String tokenPart) {
        byte[] decoded = Base64.getUrlDecoder().decode(tokenPart);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    private Long numericClaim(String payload, String claim) {
        Matcher matcher = Pattern.compile("\"" + claim + "\":(\\d+)").matcher(payload);
        return matcher.find() ? Long.parseLong(matcher.group(1)) : null;
    }
}
