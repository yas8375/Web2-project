package com.example.movies_backend.integration;

import org.junit.jupiter.api.Tag;
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
class CheckoutIntegrationTest {

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
    void checkout_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Ali",
                                  "lastName": "Ahmed",
                                  "cardNumber": "1234567890123456",
                                  "expiration": "2030-12"
                                }
                                """))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("checkout"))
                .andExpect(jsonPath("$.endpoint").value("POST /api/checkout"));
    }

    @Test
    @Tag("phase4")
    void checkout_returnsOkAndOrderConfirmation_whenPaymentIsValid() throws Exception {
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Ali",
                                  "lastName": "Ahmed",
                                  "cardNumber": "1234567890123456",
                                  "expiration": "2030-12"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.orderId").exists());
    }

    @Test
    @Tag("phase4")
    void checkout_returnsBadRequest_whenPaymentPayloadIsInvalid() throws Exception {
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "",
                                  "lastName": "",
                                  "cardNumber": "",
                                  "expiration": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }
}
