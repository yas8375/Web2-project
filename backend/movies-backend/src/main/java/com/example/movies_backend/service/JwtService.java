package com.example.movies_backend.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Optional<JwtClaims> validateToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        String[] parts = token.split("\\.", -1);
        if (parts.length != 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            return Optional.empty();
        }

        String unsignedToken = parts[0] + "." + parts[1];
        String expectedSignature = sign(unsignedToken);
        if (!MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.US_ASCII),
                parts[2].getBytes(StandardCharsets.US_ASCII))) {
            return Optional.empty();
        }

        try {
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

            String email = stringClaim(payload, "sub");
            if (email == null || email.isBlank()) {
                email = stringClaim(payload, "email");
            }

            Long expiresAt = longClaim(payload, "exp");
            if (email == null || email.isBlank() || expiresAt == null) {
                return Optional.empty();
            }

            if (expiresAt <= Instant.now().getEpochSecond()) {
                return Optional.empty();
            }

            return Optional.of(new JwtClaims(
                    email.trim(),
                    integerClaim(payload, "customerId"),
                    longClaim(payload, "iat"),
                    expiresAt));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    public boolean isTokenValid(String token) {
        return validateToken(token).isPresent();
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

    private String stringClaim(String payload, String name) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"")
                .matcher(payload);
        return matcher.find() ? unescapeJsonString(matcher.group(1)) : null;
    }

    private Long longClaim(String payload, String name) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:\\s*(-?\\d+)")
                .matcher(payload);
        if (!matcher.find()) {
            return null;
        }
        try {
            return Long.parseLong(matcher.group(1));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Integer integerClaim(String payload, String name) {
        Long value = longClaim(payload, name);
        if (value == null || value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            return null;
        }
        return value.intValue();
    }

    private String unescapeJsonString(String value) {
        StringBuilder unescaped = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c != '\\' || i == value.length() - 1) {
                unescaped.append(c);
                continue;
            }

            char escaped = value.charAt(++i);
            switch (escaped) {
                case '"' -> unescaped.append('"');
                case '\\' -> unescaped.append('\\');
                case '/' -> unescaped.append('/');
                case 'b' -> unescaped.append('\b');
                case 'f' -> unescaped.append('\f');
                case 'n' -> unescaped.append('\n');
                case 'r' -> unescaped.append('\r');
                case 't' -> unescaped.append('\t');
                case 'u' -> {
                    if (i + 4 >= value.length()) {
                        return null;
                    }
                    try {
                        unescaped.append((char) Integer.parseInt(value.substring(i + 1, i + 5), 16));
                        i += 4;
                    } catch (NumberFormatException ex) {
                        return null;
                    }
                }
                default -> unescaped.append(escaped);
            }
        }
        return unescaped.toString();
    }

    public record JwtClaims(String email, Integer customerId, Long issuedAt, Long expiresAt) {}
}
