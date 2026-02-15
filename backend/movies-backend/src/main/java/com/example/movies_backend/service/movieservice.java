package com.example.movies_backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
