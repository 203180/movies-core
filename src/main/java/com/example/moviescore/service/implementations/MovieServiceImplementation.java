package com.example.moviescore.service.implementations;

import com.example.moviescore.exceptions.MovieNotFoundException;
import com.example.moviescore.models.Movie;
import com.example.moviescore.models.Rating;
import com.example.moviescore.models.Review;
import com.example.moviescore.models.enums.Genre;
import com.example.moviescore.repositories.MovieRepository;
import com.example.moviescore.repositories.RatingRepository;
import com.example.moviescore.repositories.ReviewRepository;
import com.example.moviescore.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MovieServiceImplementation implements MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;


    public MovieServiceImplementation(MovieRepository movieRepository, ReviewRepository reviewRepository, RatingRepository ratingRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Map<Movie, Double> getMovies(String title, Genre genre, List<Genre> genres, Integer year, Integer yearFrom, Integer yearTo) {
        List<Movie> movieList = movieRepository.findAll();

        if (title != null)
            movieList = movieList.stream().filter(movie -> movie.getTitle().contains(title)).collect(Collectors.toList());
        if (genre != null)
            movieList = movieList.stream().filter(movie -> movie.getGenre().equals(genre)).collect(Collectors.toList());
        if (genres != null)
            movieList = movieList.stream().filter(movie -> genres.contains(movie.getGenre())).collect(Collectors.toList());
        if (year != null)
            movieList = movieList.stream().filter(movie -> movie.getYearOfRelease().equals(year)).collect(Collectors.toList());
        if (yearFrom != null)
            movieList = movieList.stream().filter(movie -> movie.getYearOfRelease() >= yearFrom).collect(Collectors.toList());
        if (yearTo != null)
            movieList = movieList.stream().filter(movie -> movie.getYearOfRelease() <= yearTo).collect(Collectors.toList());

        Map<Movie, Double> finalMap = new HashMap<>();
        movieList.forEach(movie -> finalMap.put(movie, movie.getAverageRating()));

        return finalMap;
    }

    @Override
    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
    }

    @Override
    public Movie addMovie(String title, String description, Integer yearOfRelease, Genre genre) {
        return movieRepository.save(new Movie(title,description,genre,yearOfRelease));
    }

    @Override
    public void rateMovie(Integer movieId, Integer rating) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException(movieId));
        Rating newRating = new Rating(rating);
        ratingRepository.save(newRating);
        List<Rating> ratingsList = movie.getRatings();
        ratingsList.add(newRating);
        movie.setRatings(ratingsList);
    }

    @Override
    public void reviewMovie(Integer movieId, String text) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException(movieId));
        Review review = new Review(text, movieId);
        reviewRepository.save(review);
        List<Review> reviewList = movie.getReviews();
        reviewList.add(review);
        movie.setReviews(reviewList);
    }
}
