package com.example.movies_backend.service;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.movies_backend.model.Movie;
import com.example.movies_backend.repository.MovieRepository;

@Service
public class MovieService {

    private static final List<Movie> MOCK_MOVIES = List.of(
            new Movie("tt1000001", "Alpha Movie", 1990, "Director A"),
            new Movie("tt1000002", "Beta Movie", 2000, "Director B"),
            new Movie("tt1000003", "Gamma Movie", 2010, "Director C"),
            new Movie("tt1000004", "Delta Movie", 2020, "Director D"));

    private final ObjectProvider<MovieRepository> movieRepositoryProvider;

    public MovieService(ObjectProvider<MovieRepository> movieRepositoryProvider) {
        this.movieRepositoryProvider = movieRepositoryProvider;
    }

    /**
     * Logic:
     * Retrieves all movies from database through repository layer.
     *
     * Params:
     * None.
     *
     * Return:
     * List<Movie> containing all movie rows.
     */
    public List<Movie> getAllMovies() {
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            return MOCK_MOVIES;
        }
        return movieRepository.findAll();
    }

    /**
     * Logic:
     * Retrieves movies page from database to avoid loading whole dataset at once.
     *
     * Params:
     * page: 1-based page number.
     * size: number of records per page.
     *
     * Return:
     * List<Movie> for the requested page.
     */
    public List<Movie> getMoviesPage(Integer page, Integer size) {
        int safePage = page == null || page < 1 ? 1 : page;
        int safeSize = size == null || size < 1 ? 50 : size;
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            return paginate(MOCK_MOVIES, safePage, safeSize);
        }
        return movieRepository.findAll(PageRequest.of(safePage - 1, safeSize)).getContent();
    }

    /**
     * Logic:
     * Retrieves one movie by its id.
     *
     * Params:
     * movieId: primary key of the movie.
     *
     * Return:
     * Movie object if exists, otherwise null.
     */
    public Movie getMovieById(String movieId) {
        MovieRepository movieRepository = movieRepositoryProvider.getIfAvailable();
        if (movieRepository == null) {
            return MOCK_MOVIES.stream()
                    .filter(movie -> movie.getId().equals(movieId))
                    .findFirst()
                    .orElse(null);
        }
        return movieRepository.findById(movieId).orElse(null);
    }

    /**
     * Logic:
     * Returns available genres list for browsing contracts.
     *
     * Params:
     * None.
     *
     * Return:
     * List<String> with genre names.
     */
    public List<String> getGenres() {
        return List.of(
                "Action",
                "Adventure",
                "Animation",
                "Comedy",
                "Crime",
                "Drama",
                "Fantasy",
                "Horror",
                "Romance",
                "Sci-Fi",
                "Thriller");
    }

    private List<Movie> paginate(List<Movie> movies, int page, int size) {
        int from = (page - 1) * size;
        if (from >= movies.size()) {
            return List.of();
        }
        int to = Math.min(from + size, movies.size());
        return movies.subList(from, to);
    }
}
