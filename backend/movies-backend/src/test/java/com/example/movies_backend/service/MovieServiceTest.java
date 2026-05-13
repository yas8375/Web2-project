package com.example.movies_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.movies_backend.dto.SingleMovieDTO;
import com.example.movies_backend.model.Movie;
import com.example.movies_backend.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Mock
  private MovieRepository movieRepository;

  @Mock
  private ObjectProvider<MovieRepository> movieRepositoryProvider;

  @InjectMocks
  private MovieService movieService;

  private void stubRepositoryProvider() {
    when(movieRepositoryProvider.getIfAvailable()).thenReturn(movieRepository);
  }

  @Test
  void getAllMovies_returnsAllMovies() {
    stubRepositoryProvider();
    when(movieRepository.findAll())
        .thenReturn(List.of(new Movie(), new Movie()));

    List<Movie> result = movieService.getAllMovies();

    assertEquals(2, result.size());
    verify(movieRepository).findAll();
  }

  @Test
  void getMoviesPage_usesDefaultPageAndSize_whenNull() {
    stubRepositoryProvider();
    var page = new PageImpl<>(List.of(new Movie()));
    when(movieRepository.findAll(PageRequest.of(0, 20))).thenReturn(page);

    List<Movie> result = movieService.getMoviesPage(null, null);

    assertEquals(1, result.size());
    verify(movieRepository).findAll(PageRequest.of(0, 20));
  }

  @Test
  void getMoviesPage_clampsInvalidValues_toDefaults() {
    stubRepositoryProvider();
    var page = new PageImpl<>(List.of(new Movie(), new Movie()));
    when(movieRepository.findAll(PageRequest.of(0, 20))).thenReturn(page);

    List<Movie> result = movieService.getMoviesPage(0, -5);

    assertEquals(2, result.size());
    verify(movieRepository).findAll(PageRequest.of(0, 20));
  }

  @Test
  void getMoviesPage_convertsToZeroBasedPageIndex() {
    stubRepositoryProvider();
    var page = new PageImpl<>(List.of(new Movie()));
    when(movieRepository.findAll(PageRequest.of(2, 10))).thenReturn(page);

    List<Movie> result = movieService.getMoviesPage(3, 10);

    assertEquals(1, result.size());
    verify(movieRepository).findAll(PageRequest.of(2, 10));
  }

  @Test
  void getMovieById_returnsSingleMovieDetails_whenFound() {
    stubRepositoryProvider();
    Movie movie = new Movie("tt123", "Movie 123", 2005, "Director X");
    when(movieRepository.findMovieDetailsById("tt123")).thenReturn(Optional.of(movie));
    when(movieRepository.findRatingByMovieId("tt123")).thenReturn(Optional.of(8.2f));
    when(movieRepository.findGenreNamesByMovieId("tt123")).thenReturn(List.of("Drama", "Thriller"));
    when(movieRepository.findStarRowsByMovieId("tt123")).thenReturn(List.of("nm1|Star One", "nm2|Star Two"));

    SingleMovieDTO result = movieService.getMovieById("tt123");

    assertNotNull(result);
    assertEquals("tt123", result.getId());
    assertEquals("Movie 123", result.getTitle());
    assertEquals(2, result.getGenres().size());
    assertEquals("nm1", result.getStars().get(0).getId());
    verify(movieRepository).findMovieDetailsById("tt123");
  }

  @Test
  void getMovieById_returnsNull_whenNotFound() {
    stubRepositoryProvider();
    when(movieRepository.findMovieDetailsById("tt999")).thenReturn(Optional.empty());

    SingleMovieDTO result = movieService.getMovieById("tt999");

    assertNull(result);
    verify(movieRepository).findMovieDetailsById("tt999");
  }

  @Test
  void getGenres_returnsExpectedList() {
    List<String> genres = movieService.getGenres();

    assertTrue(genres.contains("Action"));
    assertTrue(genres.contains("Sci-Fi"));
    assertEquals(11, genres.size());
  }
}
