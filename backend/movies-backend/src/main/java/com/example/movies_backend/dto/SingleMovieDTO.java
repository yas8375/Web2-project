package com.example.movies_backend.dto;

import java.util.List;

public class SingleMovieDTO {
    public static class StarSummaryDTO {
        private String id;
        private String name;

        public StarSummaryDTO() {
        }

        public StarSummaryDTO(String id, String name) {
            this.id = id;
            this.name = name;
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
    }

    private String id;
    private String title;
    private Integer year;
    private String director;
    private Float rating;
    private List<String> genres;
    private List<StarSummaryDTO> stars;

    public SingleMovieDTO() {
    }

    public SingleMovieDTO(
            String id,
            String title,
            Integer year,
            String director,
            Float rating,
            List<String> genres,
            List<StarSummaryDTO> stars) {
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

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<StarSummaryDTO> getStars() {
        return stars;
    }

    public void setStars(List<StarSummaryDTO> stars) {
        this.stars = stars;
    }
}
