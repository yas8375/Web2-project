package com.example.movies_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.movies_backend.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
}
