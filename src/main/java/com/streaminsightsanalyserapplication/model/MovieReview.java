package com.streaminsightsanalyserapplication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movie_review")
public class MovieReview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "movie_id")
	private int movieId;

	@Column(name = "user_id")
	private int userId;

	@Column(name = "review")
	private String review;

	@Column(name = "rating")
	private int rating;

	public MovieReview() {
		super();
	}

	public MovieReview(int movieId, int userId, String review, int rating) {
		super();
		this.movieId = movieId;
		this.userId = userId;
		this.review = review;
		this.rating = rating;
	}

	public MovieReview(int id, int movieId, int userId, String review, int rating) {
		super();
		this.id = id;
		this.movieId = movieId;
		this.userId = userId;
		this.review = review;
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "MovieReview [id=" + id + ", movieId=" + movieId + ", userId=" + userId + ", review=" + review
				+ ", rating=" + rating + "]";
	}
}
