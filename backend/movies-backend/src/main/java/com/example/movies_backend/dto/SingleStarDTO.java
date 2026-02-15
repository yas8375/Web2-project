package com.example.movies_backend.dto;

import java.util.List;

public class SingleStarDTO {
    private String id;
    private String name;
    private Integer birthYear;
    private List<MovieSummaryDTO> movies;

    public SingleStarDTO() {
    }

    public SingleStarDTO(String id, String name, Integer birthYear, List<MovieSummaryDTO> movies) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.movies = movies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public List<MovieSummaryDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieSummaryDTO> movies) {
        this.movies = movies;
    }
}
