package com.example.movies_backend.integration;

import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.movies_backend.model.Movie;
import com.example.movies_backend.service.AuthService;
import com.example.movies_backend.service.CartService;
import com.example.movies_backend.service.CheckoutService;
import com.example.movies_backend.service.MovieService;
import com.example.movies_backend.service.StarService;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration,org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration"
})
@AutoConfigureMockMvc
class MoviesIntegrationTest {

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
    void getMovies_returnsOkAndMovieList_whenServiceReturnsMovies() throws Exception {
        when(movieService.getMoviesPage(eq(1), eq(50)))
                .thenReturn(List.of(new Movie("tt0421974", "Sky Fighters", 2005, "Gerard Pires")));

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("tt0421974"))
                .andExpect(jsonPath("$[0].title").value("Sky Fighters"))
                .andExpect(jsonPath("$[0].year").value(2005))
                .andExpect(jsonPath("$[0].director").value("Gerard Pires"));

        verify(movieService).getMoviesPage(1, 50);
    }

    @Test
    void getMovies_usesProvidedPageAndSize_whenQueryParamsPresent() throws Exception {
        when(movieService.getMoviesPage(eq(3), eq(10)))
                .thenReturn(List.of(new Movie("tt1234567", "Sample Movie", 1999, "Sample Director")));

        mockMvc.perform(get("/api/movies")
                        .param("page", "3")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("tt1234567"));

        verify(movieService).getMoviesPage(3, 10);
    }

    @Test
    void getMovieById_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {
        mockMvc.perform(get("/api/movies/tt0421974"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("movie-details"))
                .andExpect(jsonPath("$.endpoint").value("GET /api/movies/{movieId}"))
                .andExpect(jsonPath("$.movieId").value("tt0421974"));
    }

    @Test
    @Tag("phase4")
    void getMovieById_returnsOkAndMoviePayload_whenMovieExists() throws Exception {
        mockMvc.perform(get("/api/movies/tt0421974"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("tt0421974"));
    }

    @Test
    void getGenres_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("browse-genres"))
                .andExpect(jsonPath("$.endpoint").value("GET /api/genres"));
    }

    @Test
    @Tag("phase4")
    void getGenres_returnsOkAndGenresArray() throws Exception {
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").isString());
    }

    @Test
    void getTitles_returnsNotImplemented_whenEndpointIsContractOnly() throws Exception {
        mockMvc.perform(get("/api/titles"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.page").value("browse-titles"))
                .andExpect(jsonPath("$.endpoint").value("GET /api/titles"));
    }

    @Test
    @Tag("phase4")
    void getTitles_returnsOkAndTitleBuckets() throws Exception {
        mockMvc.perform(get("/api/titles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}
