package com.example.movies_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.model.Movie;
import com.example.movies_backend.service.MovieService;

/**
 * Unit tests for MovieController.
 *
 * These tests focus on the web layer (HTTP requests and responses)
 * and mock the MovieService dependency.
 */
@WebMvcTest(MovieController.class)
class MovieControllerTest {

    /**
     * MockMvc allows us to simulate HTTP requests
     * without starting a real server.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * We mock MovieService because we only want
     * to test the controller behavior, not the service logic.
     */
   @MockitoBean
private MovieService movieService;

    /**
     * Phase 4 Expected Behavior:
     * GET /api/movies should return HTTP 200 and a list of movies.
     *
     * Current Phase 3 Behavior:
     * The endpoint works but only returns basic paging results.
     * This test should PASS.
     */
    @Test
    void getMovies_shouldReturnMovieList() throws Exception {

        // Mock the service response
        when(movieService.getMoviesPage(1, 50))
                .thenReturn(List.of(new Movie(), new Movie()));

        // Perform HTTP GET request
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.length()").value(2)); // Expect 2 movies
    }

    /**
     * Phase 4 Expected Behavior:
     * GET /api/movies/{movieId} should return HTTP 200 with movie details.
     *
     * Current Phase 3 Behavior:
     * The controller returns HTTP 501 NOT_IMPLEMENTED.
     *
     * Therefore this test will FAIL until Phase 4 implementation.
     */
    @Test
    void getMovieById_shouldReturnMovieDetails_phase4Expected() throws Exception {

        // Perform HTTP request
        mockMvc.perform(get("/api/movies/tt123"))

                // We EXPECT 200 in the future implementation
                // but the controller currently returns 501
                // so this test will FAIL intentionally.
                .andExpect(status().isOk());
    }

    /**
     * Phase 4 Expected Behavior:
     * GET /api/genres should return HTTP 200 with genres list.
     *
     * Current Phase 3 Behavior:
     * Endpoint returns HTTP 501 (placeholder).
     *
     * This test is expected to FAIL until Phase 4.
     */
    @Test
    void getGenres_shouldReturnGenresList_phase4Expected() throws Exception {

        mockMvc.perform(get("/api/genres"))

                // Future expected behavior
                .andExpect(status().isOk());
    }

    /**
     * Phase 4 Expected Behavior:
     * GET /api/titles should return HTTP 200 with browse titles options.
     *
     * Current Phase 3 Behavior:
     * Endpoint returns HTTP 501 placeholder.
     *
     * This test will FAIL until the feature is implemented.
     */
    @Test
    void getTitles_shouldReturnTitleBrowseOptions_phase4Expected() throws Exception {

        mockMvc.perform(get("/api/titles"))

                // Expected future behavior
                .andExpect(status().isOk());
    }
}