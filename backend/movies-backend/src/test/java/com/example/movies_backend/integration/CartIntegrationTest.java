package com.example.movies_backend.integration;

import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.movies_backend.service.AuthService;
import com.example.movies_backend.service.CartService;
import com.example.movies_backend.service.CheckoutService;
import com.example.movies_backend.service.MovieService;
import com.example.movies_backend.service.StarService;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration,org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
})
@AutoConfigureMockMvc
class CartIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private CheckoutService checkoutService;

    @MockitoBean
    private StarService starService;

    @MockitoBean
    private AuthService authService;

    @Test
    void getCart_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("cart"))
                .andExpect(jsonPath("$.endpoint").value("GET /api/cart"))
                .andExpect(jsonPath("$.items").isArray());

        verify(cartService).getCart();
    }

    @Test
    void addToCart_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {
        when(cartService.addToCart(eq("tt0421974"), eq(1)))
                .thenReturn(Map.of("success", true));

        mockMvc.perform(post("/api/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "movieId": "tt0421974",
                                  "quantity": 1
                                }
                                """))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("cart"))
                .andExpect(jsonPath("$.endpoint").value("POST /api/cart/items"));

        verify(cartService).addToCart("tt0421974", 1);
    }

    @Test
    void updateCartItem_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {
        when(cartService.updateCartItem(eq("tt0421974"), eq(3)))
                .thenReturn(Map.of("success", true));

        mockMvc.perform(put("/api/cart/items/tt0421974")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 3
                                }
                                """))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("cart"))
                .andExpect(jsonPath("$.endpoint").value("PUT /api/cart/items/{movieId}"))
                .andExpect(jsonPath("$.movieId").value("tt0421974"));

        verify(cartService).updateCartItem("tt0421974", 3);
    }

    @Test
    void removeCartItem_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {
        when(cartService.removeCartItem(eq("tt0421974")))
                .thenReturn(Map.of("success", true));

        mockMvc.perform(delete("/api/cart/items/tt0421974"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("cart"))
                .andExpect(jsonPath("$.endpoint").value("DELETE /api/cart/items/{movieId}"))
                .andExpect(jsonPath("$.movieId").value("tt0421974"));

        verify(cartService).removeCartItem("tt0421974");
    }

    @Test
    @Tag("phase4")
    void getCart_returnsOkAndCartItems_whenCartIsImplemented() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.total").exists());
    }

    @Test
    @Tag("phase4")
    void addToCart_returnsOkAndUpdatedItem_whenValidPayloadProvided() throws Exception {
        mockMvc.perform(post("/api/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "movieId": "tt0421974",
                                  "quantity": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.item.movieId").value("tt0421974"));
    }

    @Test
    @Tag("phase4")
    void updateCartItem_returnsOkAndUpdatedQuantity_whenValidPayloadProvided() throws Exception {
        mockMvc.perform(put("/api/cart/items/tt0421974")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "quantity": 2
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.item.movieId").value("tt0421974"))
                .andExpect(jsonPath("$.item.quantity").value(2));
    }

    @Test
    @Tag("phase4")
    void removeCartItem_returnsOkAndConfirmation_whenMovieExistsInCart() throws Exception {
        mockMvc.perform(delete("/api/cart/items/tt0421974"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.removedMovieId").value("tt0421974"));
    }
}
