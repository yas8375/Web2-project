package com.example.movies_backend.dto;

public class GenreDT {
    private Integer id;
    private String name;

    public GenreDT() {
    }

    public GenreDT(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
