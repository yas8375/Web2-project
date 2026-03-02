package com.example.movies_backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CheckoutServiceTest {

  /**
   * Phase 3 Current Behavior:
   * checkout() is a placeholder and returns null.
   */
  @Test
  void checkout_returnsNull_inPhase3() {
    CheckoutService checkoutService = new CheckoutService();

    Map<String, Object> result = checkoutService.checkout(
        "Rick",
        "Carter",
        "6831232434544301",
        "2006/06/08"
    );
    assertNull(
        result,
        "Phase 3: checkout() is a placeholder and should return null"
    );
  }

  /**
   * Phase 4 Expected Behavior:
   * With valid payment info, checkout should return a non-null result map
   * (e.g., confirmation/orderId/status).
   */
  //@Disabled("Phase 4: checkout() should return confirmation data after implementation")
  @Test
  void checkout_shouldReturnResultMap_whenPaymentValid_phase4Expected() {
    CheckoutService checkoutService = new CheckoutService();

    Map<String, Object> result = checkoutService.checkout(
       "Rick",
        "Carter",
        "6831232434544301",
        "2006/06/08"
    );

    assertNotNull(
        result,
        "Phase 4 expected: checkout() should return non-null result map for valid payment"
    );
  }

  /**
   * Phase 4 Expected Behavior (recommended):
   * Missing/invalid fields should be rejected (either return error map or throw exception).
   * Here we specify it should NOT succeed (non-null success result).
   */
  //@Disabled("Phase 4: input validation not implemented yet")
  @Test
  void checkout_shouldRejectInvalidPayload_phase4Expected() {
    CheckoutService checkoutService = new CheckoutService();

    Map<String, Object> result = checkoutService.checkout(
        "",     // invalid firstName
        "",     // invalid lastName
        "",     // invalid cardNumber
        ""      // invalid expiration
    );

    // In Phase 4 you might implement: return Map.of("success", false, "error", "...") OR throw exception.
    // For now we just specify that invalid payload should not produce a successful checkout result.
    assertNotNull(result, "Phase 4 expected: invalid payload should return an error result map (not null)");
    assertEquals(false, result.get("success"), "Phase 4 expected: success should be false for invalid payload");
  }
}