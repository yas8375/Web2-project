package com.example.movies_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.movies_backend.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    @Query(
            value = """
            SELECT DISTINCT m.*
            FROM movies m
            LEFT JOIN stars_in_movies sim ON sim.movieId = m.id
            LEFT JOIN stars s ON s.id = sim.starId
            LEFT JOIN genres_in_movies gim ON gim.movieId = m.id
            LEFT JOIN genres g ON g.id = gim.genreId
            WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:year IS NULL OR m.year = :year)
              AND (:director IS NULL OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%')))
              AND (:star IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :star, '%')))
              AND (:genre IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%')))
              AND (:letter IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT(:letter, '%')))
            """,
            countQuery = """
            SELECT COUNT(DISTINCT m.id)
            FROM movies m
            LEFT JOIN stars_in_movies sim ON sim.movieId = m.id
            LEFT JOIN stars s ON s.id = sim.starId
            LEFT JOIN genres_in_movies gim ON gim.movieId = m.id
            LEFT JOIN genres g ON g.id = gim.genreId
            WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:year IS NULL OR m.year = :year)
              AND (:director IS NULL OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%')))
              AND (:star IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :star, '%')))
              AND (:genre IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%')))
              AND (:letter IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT(:letter, '%')))
            """,
            nativeQuery = true)
    Page<Movie> searchMoviesByTitle(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("director") String director,
            @Param("star") String star,
            @Param("genre") String genre,
            @Param("letter") String letter,
            Pageable pageable);

    @Query(
            value = """
            SELECT m.*
            FROM movies m
            JOIN (
                SELECT DISTINCT m.id, r.rating
                FROM movies m
                LEFT JOIN ratings r ON r.movieId = m.id
                LEFT JOIN stars_in_movies sim ON sim.movieId = m.id
                LEFT JOIN stars s ON s.id = sim.starId
                LEFT JOIN genres_in_movies gim ON gim.movieId = m.id
                LEFT JOIN genres g ON g.id = gim.genreId
                WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
                  AND (:year IS NULL OR m.year = :year)
                  AND (:director IS NULL OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%')))
                  AND (:star IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :star, '%')))
                  AND (:genre IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%')))
                  AND (:letter IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT(:letter, '%')))
            ) ranked ON ranked.id = m.id
            ORDER BY ranked.rating ASC NULLS LAST
            """,
            countQuery = """
            SELECT COUNT(DISTINCT m.id)
            FROM movies m
            LEFT JOIN stars_in_movies sim ON sim.movieId = m.id
            LEFT JOIN stars s ON s.id = sim.starId
            LEFT JOIN genres_in_movies gim ON gim.movieId = m.id
            LEFT JOIN genres g ON g.id = gim.genreId
            WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:year IS NULL OR m.year = :year)
              AND (:director IS NULL OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%')))
              AND (:star IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :star, '%')))
              AND (:genre IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%')))
              AND (:letter IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT(:letter, '%')))
            """,
            nativeQuery = true)
    Page<Movie> searchMoviesByRating(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("director") String director,
            @Param("star") String star,
            @Param("genre") String genre,
            @Param("letter") String letter,
            Pageable pageable);

    @Query(
            value = """
            SELECT m.*
            FROM movies m
            JOIN (
                SELECT DISTINCT m.id, r.rating
                FROM movies m
                LEFT JOIN ratings r ON r.movieId = m.id
                LEFT JOIN stars_in_movies sim ON sim.movieId = m.id
                LEFT JOIN stars s ON s.id = sim.starId
                LEFT JOIN genres_in_movies gim ON gim.movieId = m.id
                LEFT JOIN genres g ON g.id = gim.genreId
                WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
                  AND (:year IS NULL OR m.year = :year)
                  AND (:director IS NULL OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%')))
                  AND (:star IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :star, '%')))
                  AND (:genre IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%')))
                  AND (:letter IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT(:letter, '%')))
            ) ranked ON ranked.id = m.id
            ORDER BY ranked.rating DESC NULLS LAST
            """,
            countQuery = """
            SELECT COUNT(DISTINCT m.id)
            FROM movies m
            LEFT JOIN stars_in_movies sim ON sim.movieId = m.id
            LEFT JOIN stars s ON s.id = sim.starId
            LEFT JOIN genres_in_movies gim ON gim.movieId = m.id
            LEFT JOIN genres g ON g.id = gim.genreId
            WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:year IS NULL OR m.year = :year)
              AND (:director IS NULL OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%')))
              AND (:star IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :star, '%')))
              AND (:genre IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%')))
              AND (:letter IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT(:letter, '%')))
            """,
            nativeQuery = true)
    Page<Movie> searchMoviesByRatingDesc(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("director") String director,
            @Param("star") String star,
            @Param("genre") String genre,
            @Param("letter") String letter,
            Pageable pageable);

    @Query(value = "SELECT r.movieId, r.rating FROM ratings r WHERE r.movieId IN (:movieIds)", nativeQuery = true)
    List<Object[]> findRatingsByMovieIds(@Param("movieIds") List<String> movieIds);

    @Query(value = """
            SELECT gim.movieId, g.id, g.name
            FROM genres g
            JOIN genres_in_movies gim ON gim.genreId = g.id
            WHERE gim.movieId IN (:movieIds)
            ORDER BY g.name
            """, nativeQuery = true)
    List<Object[]> findGenresByMovieIds(@Param("movieIds") List<String> movieIds);

    @Query(value = """
            SELECT sim.movieId, s.id, s.name
            FROM stars s
            JOIN stars_in_movies sim ON sim.starId = s.id
            WHERE sim.movieId IN (:movieIds)
            ORDER BY s.name
            """, nativeQuery = true)
    List<Object[]> findStarsByMovieIds(@Param("movieIds") List<String> movieIds);

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

    @Query(value = "SELECT g.id, g.name FROM genres g ORDER BY g.name", nativeQuery = true)
    List<Object[]> findAllGenres();

    @Query(value = "SELECT DISTINCT UPPER(SUBSTRING(m.title, 1, 1)) AS letter FROM movies m ORDER BY letter", nativeQuery = true)
    List<String> findTitleLetters();
}
