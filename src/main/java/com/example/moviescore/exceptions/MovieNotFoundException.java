package com.example.moviescore.exceptions;

public class MovieNotFoundException extends RuntimeException{
    public MovieNotFoundException(Integer id) {
        super(String.format("Movie with id %d is not found", id));
    }
}
