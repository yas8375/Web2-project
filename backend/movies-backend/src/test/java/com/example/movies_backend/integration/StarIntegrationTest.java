package com.example.movies_backend.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class StarIntegrationTest {

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
    void getStarById_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {
        mockMvc.perform(get("/api/stars/nm123"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("star-details"))
                .andExpect(jsonPath("$.endpoint").value("GET /api/stars/{starId}"))
                .andExpect(jsonPath("$.starId").value("nm123"));
    }

    @Test
    @Tag("phase4")
    void getStarById_returnsOkAndStarDetails_whenStarExists() throws Exception {
        mockMvc.perform(get("/api/stars/nm123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("nm123"))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.movies").isArray());
    }

    @Test
    @Tag("phase4")
    void getStarById_returnsNotFound_whenStarDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/stars/nm-does-not-exist"))
                .andExpect(status().isNotFound());
    }
}
