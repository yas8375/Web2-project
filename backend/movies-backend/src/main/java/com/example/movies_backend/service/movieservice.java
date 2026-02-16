package com.example.movies_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.movies_backend.model.Movie;
import com.example.movies_backend.repository.MovieRepository;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

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
        return movieRepository.findAll(PageRequest.of(safePage - 1, safeSize, Sort.by("title").ascending())).getContent();
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
}
