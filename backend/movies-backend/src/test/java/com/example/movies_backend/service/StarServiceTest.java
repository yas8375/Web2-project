package com.example.movies_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

import com.example.movies_backend.dto.SingleStarDTO;
import com.example.movies_backend.model.Movie;
import com.example.movies_backend.repository.MovieRepository;
import com.example.movies_backend.repository.StarRepository;

@ExtendWith(MockitoExtension.class)
class StarServiceTest {

    @Mock
    private StarRepository starRepository;

    @Mock
    private ObjectProvider<StarRepository> starRepositoryProvider;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ObjectProvider<MovieRepository> movieRepositoryProvider;

    private StarService starService;

    private void setupService() {
        starService = new StarService(starRepositoryProvider, movieRepositoryProvider);
    }

    private void stubBothProviders() {
        when(starRepositoryProvider.getIfAvailable()).thenReturn(starRepository);
        when(movieRepositoryProvider.getIfAvailable()).thenReturn(movieRepository);
    }

    @Test
    void getStarById_returnsStarDetails_whenFound() {
        setupService();
        stubBothProviders();
        when(starRepository.findStarRowsById("nm123"))
                .thenReturn(List.of("nm123|Star Name|1970"));
        when(starRepository.findMovieIdsByStarId("nm123"))
                .thenReturn(List.of("tt1"));
        when(movieRepository.findMovieDetailsById("tt1"))
                .thenReturn(java.util.Optional.of(new Movie("tt1", "Movie One", 2000, "Director One")));
        when(movieRepository.findRatingByMovieId("tt1"))
                .thenReturn(java.util.Optional.of(8.1f));

        SingleStarDTO result = starService.getStarById("nm123");

        assertNotNull(result);
        assertEquals("nm123", result.getId());
        assertEquals("Star Name", result.getName());
        assertEquals(1, result.getMovies().size());
        assertEquals("tt1", result.getMovies().get(0).getId());
        verify(starRepository).findStarRowsById("nm123");
    }

    @Test
    void getStarById_returnsNull_whenStarMissing() {
        setupService();
        when(starRepositoryProvider.getIfAvailable()).thenReturn(starRepository);
        when(starRepository.findStarRowsById("nm404")).thenReturn(List.of());

        SingleStarDTO result = starService.getStarById("nm404");

        assertNull(result);
        verify(starRepository).findStarRowsById("nm404");
    }
}
