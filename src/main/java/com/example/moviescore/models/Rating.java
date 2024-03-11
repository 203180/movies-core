package com.example.moviescore.models;

import lombok.Data;
import lombok.Getter;
import javax.persistence.*;

@Data
@Entity
@Getter
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer ratingId;

    private Integer rating;

    public Rating() {

    }

    public Rating(Integer rating) {
        this.rating = rating;
    }
}

