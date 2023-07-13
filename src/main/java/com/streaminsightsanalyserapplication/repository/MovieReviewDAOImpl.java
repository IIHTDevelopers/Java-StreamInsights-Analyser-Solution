package com.streaminsightsanalyserapplication.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.streaminsightsanalyserapplication.model.MovieReview;

public class MovieReviewDAOImpl implements MovieReviewDAO {

	private String dbUrl = "";
	private String dbUser = "";
	private String dbPassword = "";

	public MovieReviewDAOImpl(String dbUrl, String dbUser, String dbPassword) {
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	@Override
	public void addMovieReview(MovieReview movieReview) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO movie_review (movie_id, user_id, review, rating) VALUES (?, ?, ?, ?)")) {
			statement.setInt(1, movieReview.getMovieId());
			statement.setInt(2, movieReview.getUserId());
			statement.setString(3, movieReview.getReview());
			statement.setInt(4, movieReview.getRating());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Implement the remaining methods of MovieReviewDAO interface

	@Override
	public void updateMovieReview(MovieReview movieReview) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement(
						"UPDATE movie_review SET movie_id = ?, user_id = ?, review = ?, rating = ? WHERE id = ?")) {
			statement.setInt(1, movieReview.getMovieId());
			statement.setInt(2, movieReview.getUserId());
			statement.setString(3, movieReview.getReview());
			statement.setInt(4, movieReview.getRating());
			statement.setInt(5, movieReview.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MovieReview getMovieReviewById(int id) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM movie_review WHERE id = ?")) {
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				MovieReview movieReview = new MovieReview();
				movieReview.setId(resultSet.getInt("id"));
				movieReview.setMovieId(resultSet.getInt("movie_id"));
				movieReview.setUserId(resultSet.getInt("user_id"));
				movieReview.setReview(resultSet.getString("review"));
				movieReview.setRating(resultSet.getInt("rating"));

				return movieReview;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void deleteMovieReview(int id) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("DELETE FROM movie_review WHERE id = ?")) {
			statement.setInt(1, id);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAllMovieReviews() {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("DELETE FROM movie_review")) {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<MovieReview> getAllMovieReviews() {
		List<MovieReview> movieReviews = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM movie_review");

			while (resultSet.next()) {
				MovieReview movieReview = new MovieReview();
				movieReview.setId(resultSet.getInt("id"));
				movieReview.setMovieId(resultSet.getInt("movie_id"));
				movieReview.setUserId(resultSet.getInt("user_id"));
				movieReview.setReview(resultSet.getString("review"));
				movieReview.setRating(resultSet.getInt("rating"));

				movieReviews.add(movieReview);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movieReviews;
	}
}
