package com.example.moviescore.models.dtos;

import com.example.moviescore.models.enums.Genre;
import lombok.Getter;

@Getter
public class MovieDTO {
    @Getter
    private String title;
    private String description;
    private int yearOfRelease;
    private Genre genre;
    public MovieDTO() {
    }

    public MovieDTO(String title, String description, int yearOfRelease, Genre genre) {
        this.title = title;
        this.description = description;
        this.yearOfRelease = yearOfRelease;
        this.genre = genre;
    }
}

