package com.example.movies_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    @Column(name = "movieId", nullable = false, length = 10)
    private String movieId;

    @Column(name = "rating", nullable = false)
    private Float rating;

    @Column(name = "numVotes", nullable = false)
    private Integer numVotes;

    public Rating() {}

    public Rating(String movieId, Float rating, Integer numVotes) {
        this.movieId = movieId;
        this.rating = rating;
        this.numVotes = numVotes;
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }

    public Float getRating() { return rating; }
    public void setRating(Float rating) { this.rating = rating; }

    public Integer getNumVotes() { return numVotes; }
    public void setNumVotes(Integer numVotes) { this.numVotes = numVotes; }
}
