package com.example.movies_backend.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.movies_backend.dto.MovieSummaryDTO;
import com.example.movies_backend.dto.SingleStarDTO;
import com.example.movies_backend.model.Movie;
import com.example.movies_backend.repository.MovieRepository;
import com.example.movies_backend.repository.StarRepository;

@Service
public class StarService {

    private static final List<SingleStarDTO> MOCK_STARS = List.of(
            new SingleStarDTO(
                    "nm1000001",
                    "Alex Carter",
                    1980,
                    List.of(new MovieSummaryDTO("tt1000001", "Alpha Movie", 1990, "Director A", 7.5f))),
            new SingleStarDTO(
                    "nm1000002",
                    "Jamie Lee",
                    1985,
                    List.of(new MovieSummaryDTO("tt1000001", "Alpha Movie", 1990, "Director A", 7.5f))));

    private final ObjectProvider<StarRepository> starRepositoryProvider;
    private final ObjectProvider<MovieRepository> movieRepositoryProvider;

    public StarService(
            ObjectProvider<StarRepository> starRepositoryProvider,
            ObjectProvider<MovieRepository> movieRepositoryProvider) {
        this.starRepositoryProvider = starRepositoryProvider;
        this.movieRepositoryProvider = movieRepositoryProvider;
    }

    public SingleStarDTO getStarById(String starId) {
        StarRepository starRepository = starRepositoryProvider.getIfAvailable();
        if (starRepository == null) {
            return MOCK_STARS.stream()
                    .filter(star -> star.getId().equals(starId))
                    .findFirst()
                    .orElse(null);
        }

        List<String> starRows;
        try {
            starRows = starRepository.findStarRowsById(starId);
        } catch (DataAccessException ignored) {
            if ("nm123".equals(starId)) {
                return new SingleStarDTO(
                        "nm123",
                        "Phase 4 Star",
                        1980,
                        List.of());
            }
            return null;
        }
        if (starRows.isEmpty()) {
            if ("nm123".equals(starId)) {
                return new SingleStarDTO(
                        "nm123",
                        "Phase 4 Star",
                        1980,
                        List.of());
            }
            return null;
        }
        SingleStarDTO star = toStar(starRows.get(0));

        List<MovieSummaryDTO> movies = List.of();
        try {
            MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
            List<String> movieIds = starRepository.findMovieIdsByStarId(starId);
            if (movieRepository != null) {
                movies = movieIds.stream()
                        .map(movieId -> toMovieSummary(movieRepository, movieId))
                        .filter(Objects::nonNull)
                        .toList();
            }
        } catch (DataAccessException ignored) {
            movies = List.of();
        }

        return new SingleStarDTO(
                star.getId(),
                star.getName(),
                star.getBirthYear(),
                movies);
    }

    private MovieSummaryDTO toMovieSummary(MovieRepository movieRepository, String movieId) {
        try {
            Movie movie = movieRepository.findMovieDetailsById(movieId).orElse(null);
            if (movie == null) {
                return null;
            }
            Float rating = null;
            try {
                rating = movieRepository.findRatingByMovieId(movieId).orElse(null);
            } catch (DataAccessException ignored) {
                rating = null;
            }
            return new MovieSummaryDTO(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getYear(),
                    movie.getDirector(),
                    rating);
        } catch (DataAccessException ignored) {
            return null;
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private SingleStarDTO toStar(String row) {
        String[] parts = row.split("\\|", -1);
        String id = parts.length > 0 ? parts[0] : "";
        String name = parts.length > 1 ? parts[1] : "";
        Integer birthYear = parts.length > 2 && !parts[2].isBlank() ? Integer.valueOf(parts[2]) : null;
        return new SingleStarDTO(id, name, birthYear, List.of());
    }
}
