package com.example.movies_backend.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.movies_backend.service.MovieService;

@WebMvcTest(MovieController.class)
@Import(GlobalExceptionHandler.class)
class MovieControllerRobustnessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @MockitoBean
    private CacheManager cacheManager;

    @Test
    void getMovies_returns503AndSafeJson_whenServiceThrowsDatabaseFailure() throws Exception {
        when(movieService.searchMovies(null, null, null, null, null, null, "title", "asc", 1, 20))
                .thenThrow(new DataAccessResourceFailureException("DB is down"));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Database unavailable"))
                .andExpect(jsonPath("$.message").value("Please try again later."));
    }

    @Test
    void getMovies_returns500AndSafeJson_whenUnexpectedExceptionOccurs() throws Exception {
        when(movieService.searchMovies(null, null, null, null, null, null, "title", "asc", 1, 20))
                .thenThrow(new RuntimeException("Unexpected crash"));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Internal server error"))
                .andExpect(jsonPath("$.message").value("Unexpected failure while processing request."));
    }
}
