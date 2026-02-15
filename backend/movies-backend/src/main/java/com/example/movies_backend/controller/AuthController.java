package com.example.movies_backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    /**
     * Logic:
     * Authenticates customer using email and password.
     *
     * Params:
     * JSON body with email and password.
     *
     * Return:
     * HTTP 501 Not Implemented for Phase 2 (contract only).
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of(
                        "page", "login",
                        "endpoint", "POST /api/login",
                        "message", "You are on Login page. Authentication logic is planned for next phase."));
    }
}
