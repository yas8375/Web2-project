package com.example.movies_backend.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String secret;
    private final long expirationSeconds;

    public JwtService(
            @Value("${jwt.secret:movie-enjoy-development-secret-change-before-production-0123456789}") String secret,
            @Value("${jwt.expiration-seconds:3600}") long expirationSeconds) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret must be configured.");
        }

        this.secret = secret;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String email, Integer customerId) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required to generate a JWT.");
        }

        Instant now = Instant.now();
        String normalizedEmail = email.trim();

        long issuedAt = now.getEpochSecond();
        long expiresAt = now.plusSeconds(expirationSeconds).getEpochSecond();

        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        StringBuilder payload = new StringBuilder();
        payload.append("{\"sub\":")
                .append(jsonString(normalizedEmail))
                .append(",\"email\":")
                .append(jsonString(normalizedEmail));
        if (customerId != null) {
            payload.append(",\"customerId\":").append(customerId);
        }
        payload.append(",\"iat\":")
                .append(issuedAt)
                .append(",\"exp\":")
                .append(expiresAt)
                .append("}");

        String unsignedToken = base64Url(header) + "." + base64Url(payload.toString());
        return unsignedToken + "." + sign(unsignedToken);
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    private String base64Url(String value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to sign JWT.", ex);
        }
    }

    private String jsonString(String value) {
        StringBuilder escaped = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> escaped.append("\\\"");
                case '\\' -> escaped.append("\\\\");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (c < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
                }
            }
        }
        return escaped.append("\"").toString();
    }
}
