package com.example.movies_backend.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.movies_backend.model.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
    @Query("""
            select c
            from CreditCard c
            where replace(replace(c.id, ' ', ''), '-', '') = :normalizedId
            """)
    Optional<CreditCard> findByNormalizedId(@Param("normalizedId") String normalizedId);

    @Query("""
            select c
            from CreditCard c
            where replace(replace(c.id, ' ', ''), '-', '') = :normalizedId
              and lower(c.firstName) = lower(:firstName)
              and lower(c.lastName) = lower(:lastName)
              and c.expiration = :expiration
            """)
    Optional<CreditCard> findValidCard(
            @Param("normalizedId") String normalizedId,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("expiration") LocalDate expiration);
}
