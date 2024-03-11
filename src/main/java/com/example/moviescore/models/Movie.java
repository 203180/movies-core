package com.example.moviescore.models;

import com.example.moviescore.models.dtos.MovieDTO;
import com.example.moviescore.models.enums.Genre;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Integer yearOfRelease;

    @OneToMany
    @Setter
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany
    @Setter
    private List<Review> reviews = new ArrayList<>();

    public Double getAverageRating() {
        if (ratings.isEmpty())
            return 10D;
        Integer sum = 0;
        for (Rating rating : ratings)
            sum += rating.getRating();
        return sum.doubleValue() / ratings.size();
    }

    public Movie(MovieDTO movieDTO) {
        title = movieDTO.getTitle();
        description = movieDTO.getDescription();
        genre = movieDTO.getGenre();
        yearOfRelease = movieDTO.getYearOfRelease();
    }

    public Movie(String title, String description, Genre genre, Integer yearOfRelease) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.yearOfRelease = yearOfRelease;
    }

    public Movie() {

    }
}
