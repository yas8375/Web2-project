package com.example.movies_backend.service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

    /**
     * Logic:
     * Validates checkout/payment payload and records sale transaction.
     *
     * Params:
     * firstName: card holder first name.
     * lastName: card holder last name.
     * cardNumber: credit card number.
     * expiration: credit card expiration.
     *
     * Return:
     * Checkout result map (planned for next phase).
     */
    public Map<String, Object> checkout(
            String firstName,
            String lastName,
            String cardNumber,
            String expiration) {
        throw new UnsupportedOperationException("Planned for next phase");
    }
}
