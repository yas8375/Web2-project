package com.example.movies_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @Column(name = "id", nullable = false, length = 10)
    private String id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "director", nullable = false, length = 100)
    private String director;

    public Movie() {}

    public Movie(String id, String title, Integer year, String director) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
}
