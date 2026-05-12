package com.example.movies_backend.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.movies_backend.dto.CheckoutRequestDTO;
import com.example.movies_backend.dto.CheckoutResponseDTO;
import com.example.movies_backend.service.CheckoutService;

/**
 * Web-layer unit tests for CheckoutController.
 */
@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

  @Autowired
  private MockMvc mockMvc;

  // Replace CheckoutService bean with Mockito mock inside Spring test context
  @MockitoBean
  private CheckoutService checkoutService;

  @MockitoBean
  private CacheManager cacheManager;

  @Test
  void checkout_shouldReturn200_andCallService_whenRequestIsValid() throws Exception {
    when(checkoutService.checkout(any(CheckoutRequestDTO.class), any()))
        .thenReturn(new CheckoutResponseDTO(true, "Checkout complete", "order-123", null));

    mockMvc.perform(
            post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "firstName":"Aleen",
                      "lastName":"Test",
                      "cardNumber":"4111111111111111",
                      "expiration":"2030-12-01"
                    }
                    """)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Checkout complete"))
        .andExpect(jsonPath("$.orderId").value("order-123"));

    verify(checkoutService).checkout(
        argThat(request ->
            request != null
                && "Aleen".equals(request.getFirstName())
                && "Test".equals(request.getLastName())
                && "4111111111111111".equals(request.getCardNumber())
                && "2030-12-01".equals(request.getExpiration())),
        any());
  }

  @Test
  void checkout_shouldReturn400_whenServiceRejectsRequest() throws Exception {
    when(checkoutService.checkout(any(CheckoutRequestDTO.class), any()))
        .thenReturn(new CheckoutResponseDTO(false, "Invalid card number"));

    mockMvc.perform(
            post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "firstName":"",
                      "lastName":"",
                      "cardNumber":"bad",
                      "expiration":"12/30"
                    }
                    """)
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Invalid card number"));
  }
}
