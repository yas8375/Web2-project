package com.example.movies_backend.security;

import java.security.Principal;

public record JwtAuthenticatedCustomer(String email, Integer customerId) implements Principal {

    @Override
    public String getName() {
        return email;
    }
}
