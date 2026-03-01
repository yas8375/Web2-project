package com.example.movies_backend.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.service.CheckoutService;

/**
 * Web-layer unit tests for CheckoutController (Spring Boot 4).
 *
 * Focus:
 * - Controller HTTP contract (status code + JSON payload)
 * - Delegation to CheckoutService with fields parsed from request body
 *
 * Note:
 * This controller is currently a Phase 3 placeholder and returns 501.
 * Some tests are written for Phase 4 expected behavior and intentionally FAIL.
 */
@WebMvcTest(CheckoutController.class)
class CheckoutControllerTest {

  @Autowired
  private MockMvc mockMvc;

  // Replace CheckoutService bean with Mockito mock inside Spring test context
  @MockitoBean
  private CheckoutService checkoutService;

  /**
   * Phase 3 current behavior:
   * POST /api/checkout returns 501 NOT_IMPLEMENTED (placeholder),
   * but controller should still call checkoutService.checkout(...) with parsed fields.
   * This test should PASS now.
   */
  @Test
  void checkout_shouldReturn501_inPhase3_andCallService() throws Exception {

    mockMvc.perform(
            post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "Rick",
        "Carter",
        "6831232434544301",
        "2006/06/08"
                    }
                    """)
        )
        .andExpect(status().isNotImplemented()) // 501
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.page").value("checkout"))
        .andExpect(jsonPath("$.endpoint").value("POST /api/checkout"))
        .andExpect(jsonPath("$.message").exists());

    // Verify delegation to service contract
    verify(checkoutService).checkout(
        "Aleen",
        "Test",
        "4111111111111111",
        "12/30"
    );
  }

  /**
   * Phase 4 expected behavior:
   * POST /api/checkout should return 200 OK (or 201) with a checkout result.
   *
   * Current Phase 3 behavior:
   * Returns 501 -> so this test FAILS intentionally until implementation exists.
   */
  @Test
  void checkout_shouldReturn200_phase4Expected() throws Exception {

    mockMvc.perform(
            post("/api/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "firstName":"Aleen",
                      "lastName":"Test",
                      "cardNumber":"4111111111111111",
                      "expiration":"12/30"
                    }
                    """)
        )
        .andExpect(status().isOk()); // will fail now (actual is 501)
  }
}