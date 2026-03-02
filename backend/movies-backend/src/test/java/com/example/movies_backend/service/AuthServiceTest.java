package com.example.movies_backend.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class AuthServiceTest {

  /**
   * Phase 4 expected behavior:
   * - When user provides valid credentials, login should succeed (return true).
   * Phase 3 current behavior:
   * - login() is a placeholder and always returns false, so this test will FAIL for now (RED).
   */
  //@Disabled("Phase 3: login() is a placeholder and always returns false. Enable in Phase 4 after implementation.")
  @Test
  void login_shouldReturnTrue_whenCredentialsAreValid_phase4Expected() {
    AuthService authService = new AuthService();

    boolean ok = authService.login("valid@example.com", "correct-password");

    assertTrue(ok, "Phase 4 expected: login should return true for valid credentials");
  }

  /**
   * Phase 4 expected behavior:
   * - Invalid credentials should fail (return false).
   * This test will currently PASS because the placeholder always returns false.
   * Keeping it is still useful as a spec for the failure case.
   */
  @Test
  void login_shouldReturnFalse_whenCredentialsAreInvalid_phase4Expected() {
    AuthService authService = new AuthService();

    boolean ok = authService.login("valid@example.com", "wrong-password");

    assertFalse(ok, "Phase 4 expected: login should return false for invalid credentials");
  }

  /**
   * Phase 4 expected behavior (recommended):
   * - Null/empty email/password should be rejected (either return false or throw an exception).
   * Here we specify return false for invalid input.
   * This will PASS for now due to placeholder implementation.
   */
  @Test
  void login_shouldReturnFalse_whenEmailOrPasswordIsEmpty_phase4Expected() {
    AuthService authService = new AuthService();

    assertFalse(authService.login("", "1234"), "Empty email should be rejected");
    assertFalse(authService.login("a@b.com", ""), "Empty password should be rejected");
  }
}