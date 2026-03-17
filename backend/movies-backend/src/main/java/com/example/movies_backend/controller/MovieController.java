package com.example.movies_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movies_backend.dto.MovieListItemDTO;
import com.example.movies_backend.dto.SingleMovieDTO;
import com.example.movies_backend.service.MovieService;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class MovieController {

    @Autowired
    private MovieService movieService;

    /**
     * Logic:
     * Returns movies list and supports search/sort/pagination filters.
     *
     * Params:
     * title, year, director, star, genre, letter, sort, order, page, size (all optional).
     *
     * Return:
     * HTTP 200 with movies list in JSON format.
     */
    @GetMapping("/movies")
    public ResponseEntity<List<MovieListItemDTO>> getMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String star,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String letter,
            @RequestParam(defaultValue = "title") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "50") Integer size) {

        List<MovieListItemDTO> movies = movieService.searchMovies(
                title,
                year,
                director,
                star,
                genre,
                letter,
                sort,
                order,
                page,
                size);
        return ResponseEntity.ok(movies);
    }

    /**
     * Logic:
     * Returns one movie details by movie id.
     *
     * Params:
     * movieId path variable.
     *
     * Return:
     * HTTP 200 with movie JSON if found, HTTP 404 if not found.
     */
    @GetMapping("/movies/{movieId}")
    public ResponseEntity<?> getMovieById(@PathVariable String movieId) {
        SingleMovieDTO movie = movieService.getMovieById(movieId);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", "Movie not found",
                            "movieId", movieId));
        }
        return ResponseEntity.ok(movie);
    }

    /**
     * Logic:
     * Returns all movie genres for browse page.
     *
     * Params:
     * None.
     *
     * Return:
     * HTTP 200 with genres list.
     */
    @GetMapping("/genres")
    public ResponseEntity<?> getGenres() {
        return ResponseEntity.ok(movieService.getAllGenres());
    }

    /**
     * Logic:
     * Returns browse-by-title options.
     *
     * Params:
     * None.
     *
     * Return:
     * HTTP 200 with title letters list.
     */
    @GetMapping("/titles")
    public ResponseEntity<?> getTitles() {
        return ResponseEntity.ok(movieService.getTitleLetters());
    }
}
