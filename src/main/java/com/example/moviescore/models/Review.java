package com.example.moviescore.models;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Entity
@Getter
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer reviewId;

    private Integer movieId;

    private String text;

    public Review() {

    }

    public Review(String text, Integer movieId) {
        this.text = text;
        this.movieId = movieId;
    }
}
