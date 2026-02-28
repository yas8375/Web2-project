package com.example.movies_backend.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class StarIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Expected status (Phase 4): 200. Fails now because star-details endpoint is still not implemented.
    @Test
    @Tag("phase4")
    void getStarById_returnsOkAndStarDetails_whenImplemented() throws Exception {
        mockMvc.perform(get("/api/stars/nm123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("nm123"))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.movies").isArray());
    }

    // Expected status (Phase 4): 404. Fails now because not-found handling is still not implemented.
    @Test
    @Tag("phase4")
    void getStarById_returnsNotFound_whenStarDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/stars/nm-does-not-exist"))
                .andExpect(status().isNotFound());
    }
}
