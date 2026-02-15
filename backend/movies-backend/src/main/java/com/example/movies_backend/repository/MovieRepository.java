package com.example.movies_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movies_backend.model.Movie;

/**
 * Logic:
 * Data access layer for movies table using Spring Data JPA.
 *
 * Params:
 * Inherited JPA methods accept ids, paging/sorting, and entities as needed.
 *
 * Return:
 * Returns Movie entities and collection results from database queries.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
}
