package com.example.movies_backend.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movies_backend.dto.CheckoutRequestDTO;
import com.example.movies_backend.dto.CheckoutResponseDTO;
import com.example.movies_backend.model.CreditCard;
import com.example.movies_backend.model.Sale;
import com.example.movies_backend.repository.CreditCardRepository;
import com.example.movies_backend.repository.SaleRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CheckoutService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CartService cartService;

    public CheckoutResponseDTO checkout(CheckoutRequestDTO request, HttpSession session) {

        // ✅ تحقق من تسجيل الدخول
        Object sessionCustomer = session.getAttribute("customerId");
        if (!(sessionCustomer instanceof Number)) {
            return new CheckoutResponseDTO(false, "User not logged in");
        }
        Integer customerId = ((Number) sessionCustomer).intValue();

        // ----------------------------

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

        if (!card.getFirstName().equalsIgnoreCase(firstName) ||
            !card.getLastName().equalsIgnoreCase(lastName)) {
            return new CheckoutResponseDTO(false, "Cardholder name does not match");
        }

        if (!card.getExpiration().equals(expirationDate)) {
            return new CheckoutResponseDTO(false, "Expiration date does not match");
        }

        Map<String, Object> cartSummary = cartService.getCart(session);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items =
                (List<Map<String, Object>>) cartSummary.getOrDefault("items", new ArrayList<>());

        if (items.isEmpty()) {
            return new CheckoutResponseDTO(false, "Cart is empty");
        }

        LocalDate saleDate = LocalDate.now();
        List<Map<String, Object>> purchasedItems = new ArrayList<>();

        for (Map<String, Object> item : items) {
            String movieId = item.get("movieId") == null ? null : item.get("movieId").toString();

            if (movieId == null || movieId.isBlank()) {
                continue;
            }

            // Persist one sale row per cart line item (not per quantity),
            // matching the expected single-record checkout behavior.
            saleRepository.save(new Sale(null, customerId, movieId, saleDate));

            purchasedItems.add(item);
        }

        cartService.clearCart(session);

        String orderId = UUID.randomUUID().toString();
        return new CheckoutResponseDTO(true, "Checkout complete", orderId, purchasedItems);
    }
}
