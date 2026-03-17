package com.example.movies_backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.movies_backend.model.Customer;
import com.example.movies_backend.repository.CustomerRepository;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;

    public AuthService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Logic:
     * Validates customer login credentials.
     *
     * Params:
     * email: customer email.
     * password: customer plain-text password from request.
     *
     * Return:
     * true/false login status (planned for next phase).
     */
    public boolean login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return false;
        }

        Optional<Customer> customer = customerRepository.findFirstByEmail(email.trim());
        if (customer.isEmpty()) {
            return false;
        }

        // Current DB stores plain-text passwords (per provided dataset).
        return password.equals(customer.get().getPassword());
    }

    public Integer getCustomerIdByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        Optional<Customer> customer = customerRepository.findFirstByEmail(email.trim());
        return customer.map(Customer::getId).orElse(null);
    }
}
