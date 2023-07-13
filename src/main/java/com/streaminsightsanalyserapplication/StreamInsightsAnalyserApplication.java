package com.streaminsightsanalyserapplication;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.streaminsightsanalyserapplication.model.Movie;
import com.streaminsightsanalyserapplication.model.MovieReview;
import com.streaminsightsanalyserapplication.model.User;
import com.streaminsightsanalyserapplication.repository.MovieDAO;
import com.streaminsightsanalyserapplication.repository.MovieDAOImpl;
import com.streaminsightsanalyserapplication.repository.MovieReviewDAO;
import com.streaminsightsanalyserapplication.repository.MovieReviewDAOImpl;
import com.streaminsightsanalyserapplication.repository.UserDAO;
import com.streaminsightsanalyserapplication.repository.UserDAOImpl;

public class StreamInsightsAnalyserApplication {
	private static final String DB_URL;
	private static final String DB_NAME;
	private static final String DB_USER;
	private static final String DB_PASSWORD;

	private static final String CREATE_DATABASE_QUERY = "CREATE DATABASE IF NOT EXISTS ";
	private static final String CREATE_MOVIE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS movie (id INT AUTO_INCREMENT PRIMARY KEY, "
			+ "name VARCHAR(255), genre VARCHAR(255), director VARCHAR(255), star_cast VARCHAR(255), "
			+ "length INT, certificate VARCHAR(255))";
	private static final String CREATE_USER_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS user (id INT AUTO_INCREMENT PRIMARY KEY, "
			+ "name VARCHAR(255), age INT, gender VARCHAR(255))";
	private static final String CREATE_MOVIE_REVIEW_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS movie_review "
			+ "(id INT AUTO_INCREMENT PRIMARY KEY, movie_id INT, user_id INT, review VARCHAR(255), rating INT)";

	private static final Scanner scanner = new Scanner(System.in);
	private static MovieDAO movieDAO;
	private static UserDAO userDAO;
	private static MovieReviewDAO movieReviewDAO;

	static {
		Properties properties = new Properties();
		try (InputStream inputStream = StreamInsightsAnalyserApplication.class.getClassLoader()
				.getResourceAsStream("application.properties")) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DB_URL = properties.getProperty("db.url");
		DB_NAME = properties.getProperty("db.database");
		DB_USER = properties.getProperty("db.username");
		DB_PASSWORD = properties.getProperty("db.password");
	}

	public static void main(String[] args) {
		initializeDAO();

		createDatabaseIfNotExists();
		createTablesIfNotExists();

		showOptions();
	}

	private static void initializeDAO() {
		movieDAO = new MovieDAOImpl(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
		userDAO = new UserDAOImpl(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
		movieReviewDAO = new MovieReviewDAOImpl(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
	}

	private static void createDatabaseIfNotExists() {
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				Statement statement = connection.createStatement()) {
			statement.executeUpdate(CREATE_DATABASE_QUERY + DB_NAME);
			System.out.println("Database created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void createTablesIfNotExists() {
		try (Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
				Statement statement = connection.createStatement()) {
			statement.executeUpdate(CREATE_MOVIE_TABLE_QUERY);
			statement.executeUpdate(CREATE_USER_TABLE_QUERY);
			statement.executeUpdate(CREATE_MOVIE_REVIEW_TABLE_QUERY);
			System.out.println("Tables created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void showOptions() {
		while (true) {
			System.out.println("Select an option:");
			System.out.println("1. Add a new movie");
			System.out.println("2. Add a new user");
			System.out.println("3. Add a movie review");
			System.out.println("4. Update a movie");
			System.out.println("5. Update a user");
			System.out.println("6. Get details of a movie");
			System.out.println("7. Get details of a user");
			System.out.println("8. Delete a movie");
			System.out.println("9. Delete a user");
			System.out.println("10. Get most watched movies");
			System.out.println("11. Sort movies by genre count");
			System.out.println("12. Get top movies watched by users of age between 25-30");
			System.out.println("13. Search movies with minimum rating of 4 by a director");
			System.out.println("14. Get lowest rated movie for an actor");
			System.out.println("15. Search movies with anime category and length under 150 minutes");
			System.out.println("16. Exit");

			int option = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (option) {
			case 1:
				addMovie();
				break;
			case 2:
				addUser();
				break;
			case 3:
				addMovieReview();
				break;
			case 4:
				updateMovie();
				break;
			case 5:
				updateUser();
				break;
			case 6:
				getMovieDetails();
				break;
			case 7:
				getUserDetails();
				break;
			case 8:
				deleteMovie();
				break;
			case 9:
				deleteUser();
				break;
			case 10:
				getMostWatchedMovies();
				break;
			case 11:
				sortMoviesByGenreCount();
				break;
			case 12:
				getTopMoviesByAgeRange();
				break;
			case 13:
				searchMoviesByDirectorAndRating();
				break;
			case 14:
				getLowestRatedMovieByActor();
				break;
			case 15:
				searchMoviesByGenreAndLength();
				break;
			case 16:
				System.out.println("Exiting...");
				return;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	private static void addMovie() {
		System.out.println("Enter movie details:");
		System.out.print("Name: ");
		String name = scanner.nextLine();
		System.out.print("Genre: ");
		String genre = scanner.nextLine();
		System.out.print("Director: ");
		String director = scanner.nextLine();
		System.out.print("Star Cast: ");
		String starCast = scanner.nextLine();
		System.out.print("Length: ");
		int length = scanner.nextInt();
		scanner.nextLine(); // Consume newline character
		System.out.print("Certificate: ");
		String certificate = scanner.nextLine();

		Movie movie = new Movie(name, genre, director, starCast, length, certificate);
		movieDAO.addMovie(movie);
		System.out.println("Movie added successfully.");
	}

	private static void addUser() {
		System.out.println("Enter user details:");
		System.out.print("Name: ");
		String name = scanner.nextLine();
		System.out.print("Age: ");
		int age = scanner.nextInt();
		scanner.nextLine(); // Consume newline character
		System.out.print("Gender: ");
		String gender = scanner.nextLine();

		User user = new User(name, age, gender);
		userDAO.addUser(user);
		System.out.println("User added successfully.");
	}

	private static void addMovieReview() {
		System.out.println("Enter movie review details:");
		System.out.print("Movie ID: ");
		int movieId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character
		System.out.print("User ID: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character
		System.out.print("Review: ");
		String review = scanner.nextLine();
		System.out.print("Rating: ");
		int rating = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		MovieReview movieReview = new MovieReview(movieId, userId, review, rating);
		movieReviewDAO.addMovieReview(movieReview);
		System.out.println("Movie review added successfully.");
	}

	private static void updateMovie() {
		System.out.print("Enter the ID of the movie to update: ");
		int movieId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		Movie movie = movieDAO.getMovieById(movieId);
		if (movie != null) {
			System.out.println("Enter updated movie details:");
			System.out.print("Name: ");
			String name = scanner.nextLine();
			System.out.print("Genre: ");
			String genre = scanner.nextLine();
			System.out.print("Director: ");
			String director = scanner.nextLine();
			System.out.print("Star Cast: ");
			String starCast = scanner.nextLine();
			System.out.print("Length: ");
			int length = scanner.nextInt();
			scanner.nextLine(); // Consume newline character
			System.out.print("Certificate: ");
			String certificate = scanner.nextLine();

			movie.setName(name);
			movie.setGenre(genre);
			movie.setDirector(director);
			movie.setStarCast(starCast);
			movie.setLength(length);
			movie.setCertificate(certificate);

			movieDAO.updateMovie(movie);
			System.out.println("Movie updated successfully.");
		} else {
			System.out.println("Movie not found.");
		}
	}

	private static void updateUser() {
		System.out.print("Enter the ID of the user to update: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		User user = userDAO.getUserById(userId);
		if (user != null) {
			System.out.println("Enter updated user details:");
			System.out.print("Name: ");
			String name = scanner.nextLine();
			System.out.print("Age: ");
			int age = scanner.nextInt();
			scanner.nextLine(); // Consume newline character
			System.out.print("Gender: ");
			String gender = scanner.nextLine();

			user.setName(name);
			user.setAge(age);
			user.setGender(gender);

			userDAO.updateUser(user);
			System.out.println("User updated successfully.");
		} else {
			System.out.println("User not found.");
		}
	}

	private static void getMovieDetails() {
		System.out.print("Enter the ID of the movie: ");
		int movieId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		Movie movie = movieDAO.getMovieById(movieId);
		if (movie != null) {
			System.out.println(movie);
		} else {
			System.out.println("Movie not found.");
		}
	}

	private static void getUserDetails() {
		System.out.print("Enter the ID of the user: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		User user = userDAO.getUserById(userId);
		if (user != null) {
			System.out.println(user);
		} else {
			System.out.println("User not found.");
		}
	}

	private static void deleteMovie() {
		System.out.print("Enter the ID of the movie to delete: ");
		int movieId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		movieDAO.deleteMovie(movieId);
		System.out.println("Movie deleted successfully.");
	}

	private static void deleteUser() {
		System.out.print("Enter the ID of the user to delete: ");
		int userId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		userDAO.deleteUser(userId);
		System.out.println("User deleted successfully.");
	}

	private static void getMostWatchedMovies() {
		List<Movie> mostWatchedMovies = movieDAO.getMostWatchedMovies();
		if (!mostWatchedMovies.isEmpty()) {
			System.out.println("Most watched movies:");
			for (Movie movie : mostWatchedMovies) {
				System.out.println(movie);
			}
		} else {
			System.out.println("No movies found.");
		}
	}

	private static void sortMoviesByGenreCount() {
		List<Movie> sortedMovies = movieDAO.sortMoviesByGenreCount();
		if (!sortedMovies.isEmpty()) {
			System.out.println("Movies sorted by genre count:");
			for (Movie movie : sortedMovies) {
				System.out.println(movie);
			}
		} else {
			System.out.println("No movies found.");
		}
	}

	private static void getTopMoviesByAgeRange() {
		System.out.print("Enter the minimum age: ");
		int minAge = scanner.nextInt();
		scanner.nextLine(); // Consume newline character
		System.out.print("Enter the maximum age: ");
		int maxAge = scanner.nextInt();
		scanner.nextLine(); // Consume newline character
		System.out.print("Enter the limit: ");
		int limit = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		List<Movie> topMovies = movieDAO.getTopMoviesByAgeRange(minAge, maxAge, limit);
		if (!topMovies.isEmpty()) {
			System.out.println("Top movies for the age range " + minAge + " - " + maxAge + ":");
			for (Movie movie : topMovies) {
				System.out.println(movie);
			}
		} else {
			System.out.println("No movies found.");
		}
	}

	private static void searchMoviesByDirectorAndRating() {
		System.out.print("Enter the director: ");
		String director = scanner.nextLine();
		System.out.print("Enter the minimum rating: ");
		int rating = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		List<Movie> movies = movieDAO.searchMoviesByDirectorAndRating(director, rating);
		if (!movies.isEmpty()) {
			System.out.println("Movies directed by " + director + " with a minimum rating of " + rating + ":");
			for (Movie movie : movies) {
				System.out.println(movie);
			}
		} else {
			System.out.println("No movies found.");
		}
	}

	private static void getLowestRatedMovieByActor() {
		System.out.print("Enter the actor: ");
		String actor = scanner.nextLine();

		Movie lowestRatedMovie = movieDAO.getLowestRatedMovieByActor(actor);
		if (lowestRatedMovie != null) {
			System.out.println("Lowest rated movie for actor " + actor + ":");
			System.out.println(lowestRatedMovie);
		} else {
			System.out.println("No movie found.");
		}
	}

	private static void searchMoviesByGenreAndLength() {
		System.out.print("Enter the genre: ");
		String genre = scanner.nextLine();
		System.out.print("Enter the maximum length: ");
		int maxLength = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		List<Movie> movies = movieDAO.searchMoviesByGenreAndLength(genre, maxLength);
		if (!movies.isEmpty()) {
			System.out.println("Movies with genre " + genre + " and length under " + maxLength + " minutes:");
			for (Movie movie : movies) {
				System.out.println(movie);
			}
		} else {
			System.out.println("No movies found.");
		}
	}
}
