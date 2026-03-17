package com.example.movies_backend.dto;

import java.util.List;

public class MovieListItemDTO {
    private String id;
    private String title;
    private Integer year;
    private String director;
    private Float rating;
    private List<GenreDTO> genres;
    private List<StarDTO> stars;

    public MovieListItemDTO(
            String id,
            String title,
            Integer year,
            String director,
            Float rating,
            List<GenreDTO> genres,
            List<StarDTO> stars) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.genres = genres;
        this.stars = stars;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public Float getRating() {
        return rating;
    }

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public List<StarDTO> getStars() {
        return stars;
    }
}
