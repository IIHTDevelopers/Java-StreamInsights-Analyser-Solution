package com.streaminsightsanalyserapplication.repository;

import java.util.List;

import com.streaminsightsanalyserapplication.model.Movie;

public interface MovieDAO {
	void addMovie(Movie movie);

	void updateMovie(Movie movie);

	Movie getMovieById(int id);

	void deleteMovie(int id);

	List<Movie> getMostWatchedMovies();

	List<Movie> sortMoviesByGenreCount();

	List<Movie> getTopMoviesByAgeRange(int minAge, int maxAge, int limit);

	List<Movie> searchMoviesByDirectorAndRating(String director, int rating);

	Movie getLowestRatedMovieByActor(String actor);

	List<Movie> searchMoviesByGenreAndLength(String genre, int maxLength);

	void deleteAllMovies();

	List<Movie> getAllMovies();
}
