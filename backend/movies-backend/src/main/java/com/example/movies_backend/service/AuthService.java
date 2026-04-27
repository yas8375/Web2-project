package com.example.movies_backend.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movies_backend.dto.SignupRequestDTO;
import com.example.movies_backend.model.CreditCard;
import com.example.movies_backend.model.Customer;
import com.example.movies_backend.repository.CreditCardRepository;
import com.example.movies_backend.repository.CustomerRepository;

@Service
public class AuthService {

    @Autowired(required = false)
    private CustomerRepository customerRepository;

    @Autowired(required = false)
    private CreditCardRepository creditCardRepository;

    public boolean login(String email, String password) {
        if (customerRepository == null || email == null || email.isBlank() || password == null || password.isBlank()) {
            return false;
        }

        Optional<Customer> customer = customerRepository.findFirstByEmail(email.trim());
        if (customer.isEmpty()) {
            return false;
        }

        return password.equals(customer.get().getPassword());
    }

    public Integer getCustomerIdByEmail(String email) {
        if (customerRepository == null || email == null || email.isBlank()) {
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

        if (customerRepository == null || creditCardRepository == null) {
            throw new IllegalStateException("Signup is unavailable while the mock backend profile is active.");
        }

        String firstName = trimToNull(request.getFirstName());
        String lastName = trimToNull(request.getLastName());
        String address = trimToNull(request.getAddress());
        String creditCardId = trimToNull(request.getCreditCardId());
        String expiration = trimToNull(request.getExpiration());
        String email = trimToNull(request.getEmail());
        String password = trimToNull(request.getPassword());
        String confirmPassword = trimToNull(request.getConfirmPassword());

        if (firstName == null || lastName == null || address == null) {
            throw new IllegalArgumentException("First name, last name, and address are required.");
        }

        if (creditCardId == null || !creditCardId.replaceAll("[\\s-]+", "").matches("^\\d{1,20}$")) {
            throw new IllegalArgumentException("A valid credit card number is required.");
        }

        LocalDate expirationDate;
        try {
            expirationDate = LocalDate.parse(expiration);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Expiration date must use YYYY-MM-DD.");
        }

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

        String normalizedCardId = creditCardId.replaceAll("[\\s-]+", "");
        if (creditCardRepository.findByNormalizedId(normalizedCardId).isPresent()) {
            throw new IllegalArgumentException("Credit card already exists.");
        }

        CreditCard creditCard = new CreditCard(
                normalizedCardId,
                firstName,
                lastName,
                expirationDate);
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

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
