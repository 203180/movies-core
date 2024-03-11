package com.example.moviescore.service;

import com.example.moviescore.models.Movie;
import com.example.moviescore.models.enums.Genre;

import java.util.List;
import java.util.Map;

public interface MovieService {

    List<Movie> getMovies();

    Map<Movie, Double> getMovies(String title, Genre genre, List<Genre> genres, Integer year, Integer yearFrom, Integer yearTo);

    Movie getMovieById(Integer id);

    Movie addMovie(String title, String description, Integer yearOfRelease, Genre genre);

    void rateMovie(Integer movieId, Integer rating);

    void reviewMovie(Integer movieId, String text);

}
