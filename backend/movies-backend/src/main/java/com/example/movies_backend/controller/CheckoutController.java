package com.example.movies_backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movies_backend.service.CheckoutService;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
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
    public ResponseEntity<?> checkout(@RequestBody Map<String, Object> body) {
        // Service Contract:
        checkoutService.checkout(
            (String) body.get("firstName"),
            (String) body.get("lastName"),
            (String) body.get("cardNumber"),
            (String) body.get("expiration")
        );

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of(
                        "page", "checkout",
                        "endpoint", "POST /api/checkout",
                        "message", "You are on Checkout page. Payment and order logic is planned for next phase."));
    }
}
