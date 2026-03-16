package com.example.movies_backend.service;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CartServiceTest {

    @SuppressWarnings("unchecked")
private CartService createCartService() {
    ObjectProvider<MovieService> movieServiceProvider = mock(ObjectProvider.class);
    when(movieServiceProvider.getIfAvailable()).thenReturn(null);
    return new CartService(movieServiceProvider);
}
    /**
     * Phase 4 Expected Behavior:
     * getCart() should return a non-null cart object.
     * Disabled until implementation is completed.
     */
    //@Disabled("Phase 4: getCart() should return cart data after implementation")
    @Test
    void getCart_shouldReturnCartObject() {

CartService cartService = createCartService();
        HttpSession session = mock(HttpSession.class);

        Map<String, Object> cart = cartService.getCart(session);

        assertNotNull(cart);
    }

    /**
     * Phase 4 Expected Behavior:
     * addToCart should return operation result map.
     */
    //@Disabled("Phase 4: addToCart() not implemented yet")
   @Test
    void addToCart_shouldReturnResultMap() {

        CartService cartService = createCartService();
        HttpSession session = mock(HttpSession.class);

        Map<String, Object> result =
                cartService.addToCart("tt123", 2, session);

        assertNotNull(result);
    }


    /**
     * Phase 4 Expected Behavior:
     * updateCartItem should return updated cart info.
     */
    //@Disabled("Phase 4: updateCartItem() not implemented yet")
    @Test
    void updateCartItem_shouldReturnResultMap() {

        CartService cartService = createCartService();
        HttpSession session = mock(HttpSession.class);

        Map<String, Object> result =
                cartService.updateCartItem("tt123", 5, session);

        assertNotNull(result);
    }

    /**
     * Phase 4 Expected Behavior:
     * removeCartItem should return operation result map.
     */
    //@Disabled("Phase 4: removeCartItem() not implemented yet")
    @Test
    void removeCartItem_shouldReturnResultMap() {

        CartService cartService = createCartService();
        HttpSession session = mock(HttpSession.class);

        Map<String, Object> result =
                cartService.removeCartItem("tt123", session);

        assertNotNull(result);
    }
}