package com.streaminsightsanalyserapplication.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.streaminsightsanalyserapplication.model.Movie;

public class MovieDAOImpl implements MovieDAO {

	private final String dbUrl;
	private final String dbUser;
	private final String dbPassword;

	public MovieDAOImpl(String dbUrl, String dbUser, String dbPassword) {
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	@Override
	public void addMovie(Movie movie) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO movie (name, genre, director, star_cast, length, certificate) VALUES (?, ?, ?, ?, ?, ?)")) {
			statement.setString(1, movie.getName());
			statement.setString(2, movie.getGenre());
			statement.setString(3, movie.getDirector());
			statement.setString(4, movie.getStarCast());
			statement.setInt(5, movie.getLength());
			statement.setString(6, movie.getCertificate());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateMovie(Movie movie) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement(
						"UPDATE movie SET name = ?, genre = ?, director = ?, star_cast = ?, length = ?, certificate = ? WHERE id = ?")) {
			statement.setString(1, movie.getName());
			statement.setString(2, movie.getGenre());
			statement.setString(3, movie.getDirector());
			statement.setString(4, movie.getStarCast());
			statement.setInt(5, movie.getLength());
			statement.setString(6, movie.getCertificate());
			statement.setInt(7, movie.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Movie getMovieById(int id) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM movie WHERE id = ?")) {
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Movie movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setName(resultSet.getString("name"));
				movie.setGenre(resultSet.getString("genre"));
				movie.setDirector(resultSet.getString("director"));
				movie.setStarCast(resultSet.getString("star_cast"));
				movie.setLength(resultSet.getInt("length"));
				movie.setCertificate(resultSet.getString("certificate"));

				return movie;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void deleteMovie(int id) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("DELETE FROM movie WHERE id = ?")) {
			statement.setInt(1, id);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Movie> getMostWatchedMovies() {
		List<Movie> movies = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(
					"SELECT m.*, COUNT(*) AS watch_count FROM movie m INNER JOIN movie_review mr ON m.id = mr.movie_id GROUP BY m.id ORDER BY watch_count DESC LIMIT 10");

			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setName(resultSet.getString("name"));
				movie.setGenre(resultSet.getString("genre"));
				movie.setDirector(resultSet.getString("director"));
				movie.setStarCast(resultSet.getString("star_cast"));
				movie.setLength(resultSet.getInt("length"));
				movie.setCertificate(resultSet.getString("certificate"));

				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movies;
	}

	@Override
	public List<Movie> sortMoviesByGenreCount() {
		List<Movie> movies = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery(
					"SELECT m.*, COUNT(*) AS genre_count FROM movie m GROUP BY m.id, m.name, m.genre, m.director, m.star_cast, m.length, m.certificate ORDER BY genre_count DESC");

			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setName(resultSet.getString("name"));
				movie.setGenre(resultSet.getString("genre"));
				movie.setDirector(resultSet.getString("director"));
				movie.setStarCast(resultSet.getString("star_cast"));
				movie.setLength(resultSet.getInt("length"));
				movie.setCertificate(resultSet.getString("certificate"));

				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movies;
	}

	@Override
	public List<Movie> getTopMoviesByAgeRange(int minAge, int maxAge, int limit) {
		List<Movie> movies = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement(
						"SELECT m.*, COUNT(*) AS view_count FROM movie m INNER JOIN movie_review mr ON m.id = mr.movie_id INNER JOIN user u ON mr.user_id = u.id WHERE u.age >= ? AND u.age <= ? GROUP BY m.id ORDER BY view_count DESC LIMIT ?")) {
			statement.setInt(1, minAge);
			statement.setInt(2, maxAge);
			statement.setInt(3, limit);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setName(resultSet.getString("name"));
				movie.setGenre(resultSet.getString("genre"));
				movie.setDirector(resultSet.getString("director"));
				movie.setStarCast(resultSet.getString("star_cast"));
				movie.setLength(resultSet.getInt("length"));
				movie.setCertificate(resultSet.getString("certificate"));

				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movies;
	}

	@Override
	public List<Movie> searchMoviesByDirectorAndRating(String director, int rating) {
		List<Movie> movies = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement(
						"SELECT m.* FROM movie m INNER JOIN movie_review mr ON m.id = mr.movie_id WHERE m.director LIKE ? AND mr.rating >= ?")) {
			statement.setString(1, "%" + director + "%");
			statement.setInt(2, rating);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setName(resultSet.getString("name"));
				movie.setGenre(resultSet.getString("genre"));
				movie.setDirector(resultSet.getString("director"));
				movie.setStarCast(resultSet.getString("star_cast"));
				movie.setLength(resultSet.getInt("length"));
				movie.setCertificate(resultSet.getString("certificate"));

				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movies;
	}

	@Override
	public Movie getLowestRatedMovieByActor(String actor) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement(
						"SELECT m.* FROM movie m INNER JOIN movie_review mr ON m.id = mr.movie_id WHERE m.id IN (SELECT id FROM movie WHERE star_cast LIKE ?) AND mr.rating IS NOT NULL ORDER BY mr.rating ASC LIMIT 1")) {
			statement.setString(1, "%" + actor + "%");

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				Movie movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setName(resultSet.getString("name"));
				movie.setGenre(resultSet.getString("genre"));
				movie.setDirector(resultSet.getString("director"));
				movie.setStarCast(resultSet.getString("star_cast"));
				movie.setLength(resultSet.getInt("length"));
				movie.setCertificate(resultSet.getString("certificate"));

				return movie;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Movie> searchMoviesByGenreAndLength(String genre, int maxLength) {
		List<Movie> movies = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM movie WHERE genre = ? AND length <= ?")) {
			statement.setString(1, genre);
			statement.setInt(2, maxLength);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setName(resultSet.getString("name"));
				movie.setGenre(resultSet.getString("genre"));
				movie.setDirector(resultSet.getString("director"));
				movie.setStarCast(resultSet.getString("star_cast"));
				movie.setLength(resultSet.getInt("length"));
				movie.setCertificate(resultSet.getString("certificate"));

				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movies;
	}

	@Override
	public void deleteAllMovies() {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("DELETE FROM movie")) {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Movie> getAllMovies() {
		List<Movie> movies = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM movie");

			while (resultSet.next()) {
				Movie movie = new Movie();
				movie.setId(resultSet.getInt("id"));
				movie.setName(resultSet.getString("name"));
				movie.setGenre(resultSet.getString("genre"));
				movie.setDirector(resultSet.getString("director"));
				movie.setStarCast(resultSet.getString("star_cast"));
				movie.setLength(resultSet.getInt("length"));
				movie.setCertificate(resultSet.getString("certificate"));

				movies.add(movie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return movies;
	}
}
