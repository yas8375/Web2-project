package com.example.movies_backend.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movies_backend.dto.SignupRequestDTO;
import com.example.movies_backend.service.AuthService;
import com.example.movies_backend.service.JwtService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/signup/check-email")
    public ResponseEntity<?> checkEmailAvailability(@RequestParam String email) {
        if (email == null || email.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email is required."));
        }

        boolean exists = authService.emailExists(email);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
    @Autowired
    private JwtService jwtService;

    /**
     * Logic:
     * Authenticates customer using email and password.
     *
     * Params:
     * JSON body with email and password.
     *
     * Return:
     * 200 OK on success, 401 Unauthorized on invalid credentials,
     * 400 Bad Request if email/password missing.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        String email = body == null ? null : body.get("email");
        String password = body == null ? null : body.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email and password are required."));
        }

        boolean ok = authService.login(email, password);
        if (!ok) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password."));
        }

        Integer customerId = authService.getCustomerIdByEmail(email);
        if (customerId != null) {
            session.setAttribute("customerId", customerId);
        }

        String token = jwtService.generateToken(email, customerId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("expiresInSeconds", jwtService.getExpirationSeconds());
        if (customerId != null) {
            response.put("customerId", customerId);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO request, HttpSession session) {
        try {
            Integer customerId = authService.signup(request);
            session.setAttribute("customerId", customerId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Account created successfully.", "customerId", customerId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
