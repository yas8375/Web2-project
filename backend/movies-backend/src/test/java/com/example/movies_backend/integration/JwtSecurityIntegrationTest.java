package com.example.movies_backend.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.service.JwtService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class JwtSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    void protectedApiWithoutToken_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedApiWithInvalidToken_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/cart")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid.jwt.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedApiWithValidToken_passes() throws Exception {
        String token = jwtService.generateToken("valid@example.com", 42);

        mockMvc.perform(get("/api/cart")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray());
    }
}
