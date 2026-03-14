package com.example.movies_backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movies_backend.dto.CheckoutRequestDTO;
import com.example.movies_backend.dto.CheckoutResponseDTO;
import com.example.movies_backend.model.CreditCard;
import com.example.movies_backend.repository.CreditCardRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CheckoutService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    /**
     * Logic:
     * Validates checkout/payment payload against credit cards table.
     *
     * Params:
     * firstName: card holder first name.
     * lastName: card holder last name.
     * cardNumber: credit card number.
     * expiration: credit card expiration.
     *
     * Return:
     * Checkout result.
     */
    public CheckoutResponseDTO checkout(CheckoutRequestDTO request, HttpSession session) {
        if (request == null) {
            return new CheckoutResponseDTO(false, "Request body is required");
        }

        String firstName = request.getFirstName() == null ? "" : request.getFirstName().trim();
        String lastName = request.getLastName() == null ? "" : request.getLastName().trim();
        String cardNumber = request.getCardNumber() == null ? "" : request.getCardNumber().trim();
        String expiration = request.getExpiration() == null ? "" : request.getExpiration().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            return new CheckoutResponseDTO(false, "First name and last name are required");
        }

        String normalizedId = cardNumber.replaceAll("[\\s-]+", "");
        if (!normalizedId.matches("^\\d{1,20}$")) {
            return new CheckoutResponseDTO(false, "Invalid card number");
        }

        LocalDate expirationDate;
        try {
            expirationDate = LocalDate.parse(expiration);
        } catch (DateTimeParseException ex) {
            try {
                expirationDate = LocalDate.parse(expiration, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            } catch (DateTimeParseException ex2) {
                return new CheckoutResponseDTO(false, "Invalid expiration date format (YYYY-MM-DD)");
            }
        }

        CreditCard card = creditCardRepository.findByNormalizedId(normalizedId).orElse(null);
        if (card == null) {
            return new CheckoutResponseDTO(false, "Card number not found");
        }

        if (!card.getFirstName().equalsIgnoreCase(firstName) || !card.getLastName().equalsIgnoreCase(lastName)) {
            return new CheckoutResponseDTO(false, "Cardholder name does not match");
        }

        if (!card.getExpiration().equals(expirationDate)) {
            return new CheckoutResponseDTO(false, "Expiration date does not match");
        }

        String orderId = UUID.randomUUID().toString();
        return new CheckoutResponseDTO(true, "Checkout complete", orderId, null);
    }
}
