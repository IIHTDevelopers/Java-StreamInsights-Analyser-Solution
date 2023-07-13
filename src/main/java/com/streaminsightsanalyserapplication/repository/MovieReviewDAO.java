package com.streaminsightsanalyserapplication.repository;

import java.util.List;

import com.streaminsightsanalyserapplication.model.MovieReview;

public interface MovieReviewDAO {
	void addMovieReview(MovieReview movieReview);

	void deleteMovieReview(int id);

	void updateMovieReview(MovieReview movieReview);

	MovieReview getMovieReviewById(int id);

	void deleteAllMovieReviews();

	List<MovieReview> getAllMovieReviews();
}
