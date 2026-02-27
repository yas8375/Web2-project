package com.example.movies_backend.integration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class LoginIntegrationTest {

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
    void login_returnsNotImplemented_whenLoginIsNotImplemented() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "user@example.com",
                                  "password": "secret"
                                }
                                """))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("login"))
                .andExpect(jsonPath("$.endpoint").value("POST /api/login"));
    }

    @Disabled("Enable when login feature is implemented. Current behavior is 501 Not Implemented.")
    @Test
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

    @Disabled("Enable when login feature is implemented. Current behavior is 501 Not Implemented.")
    @Test
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
