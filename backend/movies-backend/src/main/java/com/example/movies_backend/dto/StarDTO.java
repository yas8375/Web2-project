package com.example.movies_backend.dto;

public class StarDTO {
    private String id;
    private String name;

    public StarDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
