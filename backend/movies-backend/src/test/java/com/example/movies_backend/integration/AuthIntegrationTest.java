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

import com.example.movies_backend.model.Customer;
import com.example.movies_backend.repository.CustomerRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private void seedValidCustomer() {
        customerRepository.deleteAll();
        customerRepository.save(new Customer(
                null,
                "First",
                "Last",
                "1234",
                "Address",
                "valid@example.com",
                "correct-password"
        ));
    }

    // Expected status (Phase 4): 200. Fails now because login is still not implemented.
    @Test
    @Tag("phase4")
    void login_returnsOk_whenCredentialsAreValid() throws Exception {
        seedValidCustomer();
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
        seedValidCustomer();
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
