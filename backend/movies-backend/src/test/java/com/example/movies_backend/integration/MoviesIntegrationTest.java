package com.example.movies_backend.integration;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

import com.example.movies_backend.model.Movie;
import com.example.movies_backend.repository.MovieRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class MoviesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void seedMovies() {
        movieRepository.deleteAll();
        movieRepository.saveAll(List.of(
                new Movie("tt1000001", "Alpha Movie", 1990, "Director A"),
                new Movie("tt1000002", "Beta Movie", 2000, "Director B"),
                new Movie("tt1000003", "Gamma Movie", 2010, "Director C")));
    }

    // Expected status: 200. Verifies GET /api/movies reads seeded rows through full stack + database.
    @Test
    void getMovies_returnsMoviesFromDatabase_withDefaultPagination() throws Exception {
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].title").isString());
    }

    // Expected status: 200. Verifies DB-backed pagination for page=2 and size=2.
    @Test
    void getMovies_appliesPageAndSize_onDatabaseResults() throws Exception {
        mockMvc.perform(get("/api/movies")
                        .param("page", "2")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").isString());
    }

    // Expected status: 200. Verifies invalid page/size fallback to safe defaults in service.
    @Test
    void getMovies_fallsBackToSafeDefaults_whenInvalidPageAndSizeProvided() throws Exception {
        mockMvc.perform(get("/api/movies")
                        .param("page", "0")
                        .param("size", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    // Expected status (Phase 4): 200. Fails now because single-movie endpoint is still not implemented.
    @Test
    @Tag("phase4")
    void getMovieById_returnsOkAndMoviePayload_whenImplemented() throws Exception {
        mockMvc.perform(get("/api/movies/tt0421974"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("tt0421974"));
    }

    // Expected status (Phase 4): 200. Fails now because genres endpoint is still not implemented.
    @Test
    @Tag("phase4")
    void getGenres_returnsOkAndGenresArray_whenImplemented() throws Exception {
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").isString());
    }

    // Expected status (Phase 4): 200. Fails now because titles endpoint is still not implemented.
    @Test
    @Tag("phase4")
    void getTitles_returnsOkAndTitleBuckets_whenImplemented() throws Exception {
        mockMvc.perform(get("/api/titles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}
