package com.example.movies_backend.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Expected status (Phase 4): 200. Fails now because login is still not implemented.
    @Test
    @Tag("phase4")
    void login_returnsOk_whenCredentialsAreValid() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "valid@example.com",
                                  "password": "correct-password"
                                }
                                """))
                .andExpect(status().isOk());
    }

    // Expected status (Phase 4): 401. Fails now because login is still not implemented.
    @Test
    @Tag("phase4")
    void login_returnsUnauthorized_whenCredentialsAreInvalid() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "valid@example.com",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }
}
