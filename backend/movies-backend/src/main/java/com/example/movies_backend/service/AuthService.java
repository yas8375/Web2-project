package com.example.movies_backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movies_backend.dto.SignupRequestDTO;
import com.example.movies_backend.model.CreditCard;
import com.example.movies_backend.model.Customer;
import com.example.movies_backend.repository.CreditCardRepository;
import com.example.movies_backend.repository.CustomerRepository;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final CreditCardRepository creditCardRepository;

    public AuthService(CustomerRepository customerRepository, CreditCardRepository creditCardRepository) {
        this.customerRepository = customerRepository;
        this.creditCardRepository = creditCardRepository;
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

    @Transactional
    public Integer signup(SignupRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required.");
        }

        String firstName = trimToNull(request.getFirstName());
        String lastName = trimToNull(request.getLastName());
        String address = trimToNull(request.getAddress());
        String email = trimToNull(request.getEmail());
        String password = trimToNull(request.getPassword());
        String confirmPassword = trimToNull(request.getConfirmPassword());
        String normalizedCardId = normalizeCardId(request.getCreditCardId());

        if (firstName == null || lastName == null || address == null) {
            throw new IllegalArgumentException("First name, last name, and address are required.");
        }

        if (normalizedCardId == null || !normalizedCardId.matches("\\d{16}")) {
            throw new IllegalArgumentException("Credit card ID must be 16 digits.");
        }

        LocalDate expirationDate = parseExpiration(request.getExpiration());

        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("A valid email address is required.");
        }

        if (customerRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (password == null || password.length() < 6 || password.length() > 20) {
            throw new IllegalArgumentException("Password must be between 6 and 20 characters.");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        CreditCard creditCard = creditCardRepository.findByNormalizedId(normalizedCardId).orElse(null);
        if (creditCard == null) {
            creditCard = new CreditCard(normalizedCardId, firstName, lastName, expirationDate);
        } else if (!creditCard.getFirstName().equalsIgnoreCase(firstName)
                || !creditCard.getLastName().equalsIgnoreCase(lastName)
                || !creditCard.getExpiration().equals(expirationDate)) {
            throw new IllegalArgumentException("Credit card information does not match the existing card record.");
        }

        creditCardRepository.save(creditCard);

        Customer customer = new Customer(
                null,
                firstName,
                lastName,
                normalizedCardId,
                address,
                email,
                password);

        return customerRepository.save(customer).getId();
    }

    private LocalDate parseExpiration(String rawValue) {
        String expiration = trimToNull(rawValue);
        if (expiration == null) {
            throw new IllegalArgumentException("Expiration date is required.");
        }

        try {
            return LocalDate.parse(expiration);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Expiration date must use YYYY-MM-DD format.");
        }
    }

    private String normalizeCardId(String rawValue) {
        String value = trimToNull(rawValue);
        if (value == null) {
            return null;
        }
        return value.replaceAll("[\\s-]+", "");
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
