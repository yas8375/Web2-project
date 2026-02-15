package com.example.movies_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movies_backend.model.Movie;
import com.example.movies_backend.service.MovieService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<List<Movie>> getMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String star,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String letter,
            @RequestParam(defaultValue = "title") String sort,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        List<Movie> movies = movieService.getAllMovies();
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
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Movie not found"));
        }
        return ResponseEntity.ok(movie);
    }

    /**
     * Logic:
     * Authenticates customer using email and password.
     *
     * Params:
     * JSON body with email and password.
     *
     * Return:
     * HTTP 200 for accepted request shape (full auth validation in later phase).
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(Map.of(
                "message", "Login endpoint defined for Phase 2 contract",
                "email", body.getOrDefault("email", "")));
    }

    /**
     * Logic:
     * Returns all movie genres.
     *
     * Params:
     * None.
     *
     * Return:
     * HTTP 200 with genres list.
     */
    @GetMapping("/genres")
    public ResponseEntity<?> getGenres() {
        return ResponseEntity.ok(movieService.getGenres());
    }

    /**
     * Logic:
     * Returns single star details by id.
     *
     * Params:
     * starId path variable.
     *
     * Return:
     * HTTP 200 with placeholder payload for Phase 2.
     */
    @GetMapping("/stars/{starId}")
    public ResponseEntity<?> getStarById(@PathVariable String starId) {
        return ResponseEntity.ok(Map.of(
                "id", starId,
                "message", "Star endpoint defined for Phase 2 contract"));
    }

    /**
     * Logic:
     * Returns current shopping cart items for session.
     *
     * Params:
     * None.
     *
     * Return:
     * HTTP 200 with cart payload.
     */
    @GetMapping("/cart")
    public ResponseEntity<?> getCart() {
        return ResponseEntity.ok(Map.of(
                "items", List.of(),
                "totalItems", 0));
    }

    /**
     * Logic:
     * Adds one movie to shopping cart.
     *
     * Params:
     * JSON body with movieId and quantity.
     *
     * Return:
     * HTTP 200 with operation result.
     */
    @PostMapping("/cart/items")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of(
                "message", "Add-to-cart endpoint defined for Phase 2 contract",
                "payload", body));
    }

    /**
     * Logic:
     * Updates quantity for one cart item.
     *
     * Params:
     * movieId path variable, quantity in request body.
     *
     * Return:
     * HTTP 200 with operation result.
     */
    @PutMapping("/cart/items/{movieId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable String movieId,
            @RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of(
                "message", "Update-cart endpoint defined for Phase 2 contract",
                "movieId", movieId,
                "payload", body));
    }

    /**
     * Logic:
     * Removes one movie from shopping cart.
     *
     * Params:
     * movieId path variable.
     *
     * Return:
     * HTTP 200 with operation result.
     */
    @DeleteMapping("/cart/items/{movieId}")
    public ResponseEntity<?> removeCartItem(@PathVariable String movieId) {
        return ResponseEntity.ok(Map.of(
                "message", "Remove-cart endpoint defined for Phase 2 contract",
                "movieId", movieId));
    }

    /**
     * Logic:
     * Handles checkout request and validates payment payload shape.
     *
     * Params:
     * JSON body with payment fields.
     *
     * Return:
     * HTTP 200 with placeholder checkout result for Phase 2.
     */
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(Map.of(
                "message", "Checkout endpoint defined for Phase 2 contract",
                "payload", body));
    }
}
