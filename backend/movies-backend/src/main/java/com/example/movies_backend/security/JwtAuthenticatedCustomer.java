package com.example.movies_backend.security;

import java.security.Principal;

public record JwtAuthenticatedCustomer(String email, Integer customerId) implements Principal {  // the logged in customer identity

    @Override
    public String getName() {
        return email;
    }
}
