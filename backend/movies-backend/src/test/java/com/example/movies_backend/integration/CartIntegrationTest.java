package com.example.movies_backend.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.movies_backend.service.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class CartIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    // Expected status (Phase 4): 200. Fails now because cart retrieval is still not implemented.
    @Test
    @Tag("phase4")
    void getCart_returnsOkAndCartItems_whenImplemented() throws Exception {
        mockMvc.perform(get("/api/cart")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.total").exists());
    }

    // Expected status (Phase 4): 200. Fails now because add-to-cart logic is still not implemented.
    @Test
    @Tag("phase4")
    void addToCart_returnsOkAndUpdatedItem_whenImplemented() throws Exception {
        mockMvc.perform(post("/api/cart/items")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
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

    // Expected status (Phase 4): 200. Fails now because update-cart logic is still not implemented.
    @Test
    @Tag("phase4")
    void updateCartItem_returnsOkAndUpdatedQuantity_whenImplemented() throws Exception {
        mockMvc.perform(put("/api/cart/items/tt0421974")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
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

    // Expected status (Phase 4): 200. Fails now because remove-cart logic is still not implemented.
    @Test
    @Tag("phase4")
    void removeCartItem_returnsOkAndConfirmation_whenImplemented() throws Exception {
        mockMvc.perform(delete("/api/cart/items/tt0421974")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.removedMovieId").value("tt0421974"));
    }

    private String bearerToken() {
        return "Bearer " + jwtService.generateToken("valid@example.com", 42);
    }
}
