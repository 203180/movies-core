package com.example.moviescore.web;

import com.example.moviescore.models.Movie;
import com.example.moviescore.models.enums.Genre;
import com.example.moviescore.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "http://localhost:3000")
public class MovieRestController {

    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping
    public Movie addMovie(@RequestParam(value = "title") String title, @RequestParam(value = "description") String description, @RequestParam(value = "yearOfRelease") Integer yearOfRelease, @RequestParam(value = "genre") String genre) {
        return movieService.addMovie(title, description, yearOfRelease, Genre.valueOf(genre));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public Map<Movie, Double> getMovies(@PathVariable(required = false) String title,
                                        @PathVariable(required = false) Genre genre,
                                        @PathVariable(required = false) List<Genre> genres,
                                        @PathVariable(required = false) Integer year,
                                        @PathVariable(required = false) Integer yearFrom,
                                        @PathVariable(required = false) Integer yearTo) {
        return movieService.getMovies(title, genre, genres, year, yearFrom, yearTo);
    }

    @GetMapping("/movies/{movieId}")
    public Movie getMovie(@PathVariable Integer movieId) {
        return movieService.getMovieById(movieId);
    }
}
