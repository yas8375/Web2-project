package com.example.movies_backend.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.service.StarService;

/**
 * Web-layer unit tests for StarController (Spring Boot 4).
 *
 * Focus:
 * - HTTP contract (status code + JSON response fields)
 * - Controller delegates to StarService with the correct path variable
 */
@WebMvcTest(StarController.class)
class StarControllerTest {

  @Autowired
  private MockMvc mockMvc;

  // Replace StarService bean with Mockito mock in the Spring test context
  @MockitoBean
  private StarService starService;

  /**
   * Phase 3 current behavior:
   * GET /api/stars/{starId} returns 501 NOT_IMPLEMENTED (placeholder),
   * but controller should still call starService.getStarById(starId).
   *
   * This test should PASS now.
   */
  @Test
  void getStarById_shouldReturn501_inPhase3_andCallService() throws Exception {

    mockMvc.perform(get("/api/stars/nm0000138"))
        .andExpect(status().isNotImplemented()) // 501
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.page").value("star-details"))
        .andExpect(jsonPath("$.endpoint").value("GET /api/stars/{starId}"))
        .andExpect(jsonPath("$.starId").value("nm0000138"))
        .andExpect(jsonPath("$.message").exists());

    // Verify controller delegated to the service contract
    verify(starService).getStarById("nm0000138");
  }

  /**
   * Phase 4 expected behavior:
   * GET /api/stars/{starId} should return 200 OK with star details.
   *
   * Current Phase 3 behavior:
   * Returns 501 -> so this test FAILS intentionally until implementation exists.
   */
  @Test
  void getStarById_shouldReturn200_phase4Expected() throws Exception {

    mockMvc.perform(get("/api/stars/nm0000138"))
        .andExpect(status().isOk()); // will fail now (actual is 501)
  }
}