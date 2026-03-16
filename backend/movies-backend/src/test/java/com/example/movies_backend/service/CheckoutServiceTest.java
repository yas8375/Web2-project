package com.example.movies_backend.service;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.movies_backend.dto.CheckoutRequestDTO;
import com.example.movies_backend.dto.CheckoutResponseDTO;
import com.example.movies_backend.model.CreditCard;
import com.example.movies_backend.repository.CreditCardRepository;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

  @Mock
  private CreditCardRepository creditCardRepository;

  @InjectMocks
  private CheckoutService checkoutService;

  @Test
  void checkout_shouldReturnSuccess_whenPaymentInfoMatchesStoredCard() {
    CreditCard card = new CreditCard(
        "4111111111111111",
        "Rick",
        "Carter",
        LocalDate.of(2006, 6, 8));
    when(creditCardRepository.findByNormalizedId("4111111111111111"))
        .thenReturn(Optional.of(card));

    CheckoutRequestDTO request = new CheckoutRequestDTO(
        "Rick",
        "Carter",
        "4111 1111 1111 1111",
        "2006/06/08");
    CheckoutResponseDTO result = checkoutService.checkout(request, null);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertEquals("Checkout complete", result.getMessage());
    assertNotNull(result.getOrderId());
  }

  @Test
  void checkout_shouldRejectInvalidPayload() {
    CheckoutRequestDTO request = new CheckoutRequestDTO(
        "",
        "",
        "",
        "");
    CheckoutResponseDTO result = checkoutService.checkout(request, null);

    assertNotNull(result);
    assertFalse(result.isSuccess());
    assertEquals("First name and last name are required", result.getMessage());
  }

  @Test
  void checkout_shouldRejectUnknownCard() {
    when(creditCardRepository.findByNormalizedId("4111111111111111"))
        .thenReturn(Optional.empty());

    CheckoutRequestDTO request = new CheckoutRequestDTO(
        "Rick",
        "Carter",
        "4111111111111111",
        "2006-06-08");
    CheckoutResponseDTO result = checkoutService.checkout(request, null);

    assertNotNull(result);
    assertFalse(result.isSuccess());
    assertEquals("Card number not found", result.getMessage());
  }
}
