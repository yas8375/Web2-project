package com.example.movies_backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "customerid", nullable = false)
    private Integer customerId;

    @Column(name = "movieid", nullable = false, length = 10)
    private String movieId;

    @Column(name = "saledate", nullable = false)
    private LocalDate saleDate;

    public Sale() {}

    public Sale(Integer id, Integer customerId, String movieId, LocalDate saleDate) {
        this.id = id;
        this.customerId = customerId;
        this.movieId = movieId;
        this.saleDate = saleDate;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public LocalDate getSaleDate() { return saleDate; }
    public void setSaleDate(LocalDate saleDate) { this.saleDate = saleDate; }
}
