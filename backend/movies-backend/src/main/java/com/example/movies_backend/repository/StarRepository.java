package com.example.movies_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movies_backend.model.Star;

@Repository
public interface StarRepository extends JpaRepository<Star, String> {
    @Query(value = """
            SELECT s.id || '|' || s.name || '|' || COALESCE(CAST(s.birthyear AS TEXT), '')
            FROM stars s
            WHERE s.id = :starId
            """, nativeQuery = true)
    List<String> findStarRowsById(@Param("starId") String starId);

    @Query(value = """
            SELECT DISTINCT sim.movieId
            FROM stars_in_movies sim
            WHERE sim.starId = :starId
            ORDER BY sim.movieId
            """, nativeQuery = true)
    List<String> findMovieIdsByStarId(@Param("starId") String starId);
}
