package com.example.movies_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.movies_backend.model.Customer;
import com.example.movies_backend.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private AuthService authService;

  /**
   * Phase 4 expected behavior:
   * - When user provides valid credentials, login should succeed (return true).
   */
  @Test
  void login_shouldReturnTrue_whenCredentialsAreValid_phase4Expected() {
    when(customerRepository.findFirstByEmail("valid@example.com"))
        .thenReturn(java.util.Optional.of(new Customer(
            null,
            "First",
            "Last",
            "1234",
            "Address",
            "valid@example.com",
            "correct-password"
        )));

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
    when(customerRepository.findFirstByEmail("valid@example.com"))
        .thenReturn(java.util.Optional.of(new Customer(
            null,
            "First",
            "Last",
            "1234",
            "Address",
            "valid@example.com",
            "correct-password"
        )));

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
    assertFalse(authService.login("", "1234"), "Empty email should be rejected");
    assertFalse(authService.login("a@b.com", ""), "Empty password should be rejected");
  }
}
