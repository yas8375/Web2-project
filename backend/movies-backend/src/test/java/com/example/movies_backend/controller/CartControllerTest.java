package com.example.movies_backend.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.service.CartService;

/**
 * Web-layer unit tests for CartController (Spring Boot 4).
 *
 * What we test here:
 * - HTTP status codes (501 now vs 200 later)
 * - Response JSON contract (page/endpoint/message/movieId)
 * - Controller delegates to CartService (service contract calls)
 *
 * What we do NOT test here:
 * - Business logic / database (that's for Service tests)
 */
@WebMvcTest(CartController.class)
class CartControllerTest {

  @Autowired
  private MockMvc mockMvc;

  // Replace CartService bean with a Mockito mock inside the Spring test context
  @MockitoBean
  private CartService cartService;

  /**
   * Phase 3 current behavior:
   * GET /api/cart returns 501 as a placeholder, but should still call cartService.getCart().
   * This test should PASS now.
   */
  @Test
  void getCart_shouldReturn501_inPhase3_andCallService() throws Exception {
    mockMvc.perform(get("/api/cart"))
        .andExpect(status().isNotImplemented())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.page").value("cart"))
        .andExpect(jsonPath("$.endpoint").value("GET /api/cart"))
        .andExpect(jsonPath("$.items").isArray())
        .andExpect(jsonPath("$.message").exists());

    verify(cartService).getCart();
  }

  /**
   * Phase 4 expected behavior:
   * GET /api/cart should return 200 OK with real cart data.
   * Current Phase 3 returns 501 -> this test FAILS intentionally.
   */
  @Test
  void getCart_shouldReturn200_phase4Expected() throws Exception {
    mockMvc.perform(get("/api/cart"))
        .andExpect(status().isOk()); // will fail now (actual is 501)
  }

  /**
   * Phase 3 current behavior:
   * POST /api/cart/items returns 501 placeholder and delegates to addToCart(movieId, quantity).
   * This test should PASS now.
   */
  @Test
  void addToCart_shouldReturn501_inPhase3_andCallService() throws Exception {
    mockMvc.perform(
            post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"tt123\",\"quantity\":2}")
        )
        .andExpect(status().isNotImplemented())
        .andExpect(jsonPath("$.page").value("cart"))
        .andExpect(jsonPath("$.endpoint").value("POST /api/cart/items"))
        .andExpect(jsonPath("$.message").exists());

    verify(cartService).addToCart("tt123", 2);
  }

  /**
   * Phase 3 current behavior:
   * PUT /api/cart/items/{movieId} returns 501 placeholder and delegates to updateCartItem(movieId, quantity).
   * This test should PASS now.
   */
  @Test
  void updateCartItem_shouldReturn501_inPhase3_andCallService() throws Exception {
    mockMvc.perform(
            put("/api/cart/items/tt123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantity\":5}")
        )
        .andExpect(status().isNotImplemented())
        .andExpect(jsonPath("$.page").value("cart"))
        .andExpect(jsonPath("$.endpoint").value("PUT /api/cart/items/{movieId}"))
        .andExpect(jsonPath("$.movieId").value("tt123"))
        .andExpect(jsonPath("$.message").exists());

    verify(cartService).updateCartItem("tt123", 5);
  }

  /**
   * Phase 3 current behavior:
   * DELETE /api/cart/items/{movieId} returns 501 placeholder and delegates to removeCartItem(movieId).
   * This test should PASS now.
   */
  @Test
  void removeCartItem_shouldReturn501_inPhase3_andCallService() throws Exception {
    mockMvc.perform(delete("/api/cart/items/tt123"))
        .andExpect(status().isNotImplemented())
        .andExpect(jsonPath("$.page").value("cart"))
        .andExpect(jsonPath("$.endpoint").value("DELETE /api/cart/items/{movieId}"))
        .andExpect(jsonPath("$.movieId").value("tt123"))
        .andExpect(jsonPath("$.message").exists());

    verify(cartService).removeCartItem("tt123");
  }

  /**
   * Phase 4 expected behavior:
   * POST /api/cart/items should return 200/201 when item is added successfully.
   * Current Phase 3 returns 501 -> this test FAILS intentionally.
   */
  @Test
  void addToCart_shouldReturn200_phase4Expected() throws Exception {
    mockMvc.perform(
            post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"tt123\",\"quantity\":2}")
        )
        .andExpect(status().isOk()); // will fail now (actual is 501)
  }
}