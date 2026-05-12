package com.example.movies_backend.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.service.AuthService;
import com.example.movies_backend.service.JwtService;

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

  @MockitoBean
  private CacheManager cacheManager;

  @MockitoBean
  private JwtService jwtService;

  /**
   * Phase 4 behavior:
   * - missing email/password -> 400
   */
  @Test
  void login_shouldReturn400_whenMissingFields() throws Exception {
    mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\",\"password\":\"\"}")
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").exists());
  }

  /**
   * Phase 4 behavior:
   * - invalid credentials -> 401
   */
  @Test
  void login_shouldReturn401_whenInvalidCredentials() throws Exception {
    when(authService.login("a@a.com", "bad")).thenReturn(false);
    mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"a@a.com\",\"password\":\"bad\"}")
        )
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").exists());

    verify(authService).login("a@a.com", "bad");
  }

  /**
   * Phase 4 behavior:
   * - valid credentials -> 200
   */
  @Test
  void login_shouldReturn200_whenValidCredentials() throws Exception {
    when(authService.login("a@a.com", "123")).thenReturn(true);
    when(authService.getCustomerIdByEmail("a@a.com")).thenReturn(7);
    when(jwtService.generateToken("a@a.com", 7)).thenReturn("test.jwt.token");
    when(jwtService.getExpirationSeconds()).thenReturn(3600L);

    mockMvc.perform(
            post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"a@a.com\",\"password\":\"123\"}")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Login successful"))
        .andExpect(jsonPath("$.token").value("test.jwt.token"))
        .andExpect(jsonPath("$.tokenType").value("Bearer"))
        .andExpect(jsonPath("$.expiresInSeconds").value(3600))
        .andExpect(jsonPath("$.customerId").value(7));

    verify(authService).login("a@a.com", "123");
  }
}
