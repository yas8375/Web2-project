package com.example.movies_backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.movies_backend.model.Movie;
import com.example.movies_backend.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  // Create a fake repository (no real database will be used)
  @Mock
  private MovieRepository movieRepository;

  // Inject the fake repository into MovieService
  @InjectMocks
  private MovieService movieService;

  // Test getAllMovies method to ensure it returns all movies from the repository
  @Test
  void getAllMovies_returnsAllMovies() {
    // Arrange: mock repository behavior
    when(movieRepository.findAll())
        .thenReturn(List.of(new Movie(), new Movie()));

    // Act: call service method
    List<Movie> result = movieService.getAllMovies();

    // Assert: verify result size
    assertEquals(
        2,
        result.size(),
        "Service should return all movies from repository without modification"
    );

    // Verify that repository method was called
    verify(movieRepository).findAll();
  }

  /**
   * Test:
   * If page and size are null,
   * service should use default values (page=1, size=50).
   */
  @Test
  void getMoviesPage_usesDefaultPageAndSize_whenNull() {
    var page = new PageImpl<>(List.of(new Movie()));
    when(movieRepository.findAll(PageRequest.of(0, 50))).thenReturn(page);

    List<Movie> result = movieService.getMoviesPage(null, null);

    assertEquals(
        1,
        result.size(),
        "When page/size are null, service should default to page=1 and size=50"
    );
    verify(movieRepository).findAll(PageRequest.of(0, 50));
  }

  /**
   * Test:
   * Invalid page/size should fallback to defaults.
   */
  @Test
  void getMoviesPage_clampsInvalidValues_toDefaults() {
    var page = new PageImpl<>(List.of(new Movie(), new Movie()));
    when(movieRepository.findAll(PageRequest.of(0, 50))).thenReturn(page);

    List<Movie> result = movieService.getMoviesPage(0, -5);

    assertEquals(
        2,
        result.size(),
        "Invalid page/size should fallback to defaults (page=1, size=50)"
    );
    verify(movieRepository).findAll(PageRequest.of(0, 50));
  }

  /**
   * Test:
   * Page number should be converted from 1-based to 0-based.
   */
  @Test
  void getMoviesPage_convertsToZeroBasedPageIndex() {
    var page = new PageImpl<>(List.of(new Movie()));
    when(movieRepository.findAll(PageRequest.of(2, 10))).thenReturn(page);

    List<Movie> result = movieService.getMoviesPage(3, 10);

    assertEquals(
        1,
        result.size(),
        "Service should convert 1-based page number to 0-based index for PageRequest"
    );
    verify(movieRepository).findAll(PageRequest.of(2, 10));
  }

  /**
   * Test:
   * getMovieById should return movie if found.
   */
  @Test
  void getMovieById_returnsMovie_whenFound() {
    Movie m = new Movie();
    m.setId("tt123");

    when(movieRepository.findById("tt123")).thenReturn(Optional.of(m));

    Movie result = movieService.getMovieById("tt123");

    assertNotNull(result, "Movie should not be null when repository returns a value");
    assertEquals(
        "tt123",
        result.getId(),
        "Returned movie should have the same id requested"
    );
    verify(movieRepository).findById("tt123");
  }

  /**
   * Test:
   * getMovieById should return null if movie does not exist.
   */
  @Test
  void getMovieById_returnsNull_whenNotFound() {
    when(movieRepository.findById("tt999")).thenReturn(Optional.empty());

    Movie result = movieService.getMovieById("tt999");

    assertNull(result, "When movie id does not exist, service should return null");
    verify(movieRepository).findById("tt999");
  }

  /**
   * Test:
   * getGenres should return predefined genre list.
   */
  @Test
  void getGenres_returnsExpectedList() {
    List<String> genres = movieService.getGenres();

    assertTrue(genres.contains("Action"), "Genres list should contain 'Action'");
    assertTrue(genres.contains("Sci-Fi"), "Genres list should contain 'Sci-Fi'");
    assertEquals(
        11,
        genres.size(),
        "getGenres should return exactly 11 predefined genres"
    );
  }
}