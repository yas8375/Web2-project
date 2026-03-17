package com.example.movies_backend.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.dto.MovieListItemDTO;
import com.example.movies_backend.dto.SingleMovieDTO;
import com.example.movies_backend.service.MovieService;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @Test
    void getMovies_shouldReturnMovieList() throws Exception {
        when(movieService.searchMovies(null, null, null, null, null, null, "title", "asc", 1, 50))
                .thenReturn(List.of(
                        new MovieListItemDTO("tt1", "Alpha", 2000, "Director A", null, List.of(), List.of()),
                        new MovieListItemDTO("tt2", "Beta", 2001, "Director B", null, List.of(), List.of())));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("tt1"));
    }

    @Test
    void getMovieById_shouldReturnMovieDetails() throws Exception {
        when(movieService.getMovieById(eq("tt123"))).thenReturn(
                new SingleMovieDTO(
                        "tt123",
                        "Movie 123",
                        2005,
                        "Director X",
                        8.2f,
                        List.of("Drama"),
                        List.of(new SingleMovieDTO.StarSummaryDTO("nm1", "Star One"))));

        mockMvc.perform(get("/api/movies/tt123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("tt123"))
                .andExpect(jsonPath("$.title").value("Movie 123"))
                .andExpect(jsonPath("$.stars[0].id").value("nm1"));
    }

    @Test
    void getMovieById_shouldReturn404_whenMovieMissing() throws Exception {
        when(movieService.getMovieById("tt999")).thenReturn(null);

        mockMvc.perform(get("/api/movies/tt999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie not found"))
                .andExpect(jsonPath("$.movieId").value("tt999"));
    }
}
