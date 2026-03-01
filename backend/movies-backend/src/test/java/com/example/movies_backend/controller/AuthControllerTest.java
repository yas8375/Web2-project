package com.example.movies_backend.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.service.AuthService;

/**
 * Web-layer unit tests for AuthController (Spring Boot 4).
 * - Uses MockMvc to simulate HTTP requests (no real server).
 * - Overrides AuthService with Mockito using @MockitoBean.
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  // Replace AuthService bean in the Spring context with a Mockito mock
  @MockitoBean
  private AuthService authService;

  /**
   * Phase 3 current behavior:
   * Endpoint is a contract-only placeholder -> returns 501 NOT_IMPLEMENTED.
   *
   * We still verify that controller calls the service with email/password
   * parsed from request body.
   */
  @Test
  void login_shouldReturn501_inPhase3_andCallService() throws Exception {

    // (Optional) Define mock behavior; not required since we only verify call
    when(authService.login("a@a.com", "123")).thenReturn(false);

    mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"a@a.com\",\"password\":\"123\"}")
        )
        .andExpect(status().isNotImplemented())                 // 501
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.page").value("login"))
        .andExpect(jsonPath("$.endpoint").value("POST /api/login"))
        .andExpect(jsonPath("$.message").exists());

    // Verify controller delegated to the service
    verify(authService).login("a@a.com", "123");
  }

  /**
   * Phase 4 expected behavior:
   * Endpoint should return 200 OK (or 401 for invalid credentials) once implemented.
   *
   * Current Phase 3 behavior:
   * Controller returns 501 -> so this test FAILS intentionally until Phase 4.
   */
  @Test
  void login_shouldReturn200_phase4Expected() throws Exception {

    mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"a@a.com\",\"password\":\"123\"}")
        )
        .andExpect(status().isOk()); // will fail now (actual is 501)
  }
}