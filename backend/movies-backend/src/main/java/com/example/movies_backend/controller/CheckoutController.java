package com.example.movies_backend.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movies_backend.dto.CheckoutRequestDTO;
import com.example.movies_backend.dto.CheckoutResponseDTO;
import com.example.movies_backend.service.CheckoutService;

@RestController
@RequestMapping("/api")
@CrossOrigin(
        originPatterns = {"http://localhost:*", "http://127.0.0.1:*"},
        allowCredentials = "true")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    /**
     * Logic:
     * Handles checkout request and validates payment payload shape.
     *
     * Params:
     * JSON body with payment fields.
     *
     * Return:
     * HTTP 501 Not Implemented for Phase 2 (contract only).
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequestDTO body, HttpSession session) {
        CheckoutResponseDTO result = checkoutService.checkout(body, session);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
