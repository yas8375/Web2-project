package com.example.movies_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movies_backend.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    @Query(value = """
            SELECT DISTINCT m.*
            FROM movies m
            LEFT JOIN stars_in_movies sim ON sim.movieId = m.id
            LEFT JOIN stars s ON s.id = sim.starId
            WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:year IS NULL OR m.year = :year)
              AND (:director IS NULL OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%')))
              AND (:star IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :star, '%')))
            """, nativeQuery = true)
    List<Movie> searchMovies(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("director") String director,
            @Param("star") String star);

    @Query(value = """
            SELECT m.*
            FROM movies m
            WHERE m.id = :movieId
            """, nativeQuery = true)
    Optional<Movie> findMovieDetailsById(@Param("movieId") String movieId);

    @Query(value = """
            SELECT g.name
            FROM genres g
            JOIN genres_in_movies gim ON gim.genreId = g.id
            WHERE gim.movieId = :movieId
            ORDER BY g.name
            """, nativeQuery = true)
    List<String> findGenreNamesByMovieId(@Param("movieId") String movieId);

    @Query(value = """
            SELECT s.id || '|' || s.name
            FROM stars s
            JOIN stars_in_movies sim ON sim.starId = s.id
            WHERE sim.movieId = :movieId
            ORDER BY s.name, s.id
            """, nativeQuery = true)
    List<String> findStarRowsByMovieId(@Param("movieId") String movieId);

    @Query(value = """
            SELECT r.rating
            FROM ratings r
            WHERE r.movieId = :movieId
            """, nativeQuery = true)
    Optional<Float> findRatingByMovieId(@Param("movieId") String movieId);
}
