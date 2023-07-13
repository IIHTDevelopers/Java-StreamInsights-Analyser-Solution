package com.assessment.streaminsightsanalyserapplication.functional;

import static com.assessment.streaminsightsanalyserapplication.testutils.TestUtils.businessTestFile;
import static com.assessment.streaminsightsanalyserapplication.testutils.TestUtils.currentTest;
import static com.assessment.streaminsightsanalyserapplication.testutils.TestUtils.testReport;
import static com.assessment.streaminsightsanalyserapplication.testutils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import com.streaminsightsanalyserapplication.StreamInsightsAnalyserApplication;
import com.streaminsightsanalyserapplication.model.Movie;
import com.streaminsightsanalyserapplication.model.MovieReview;
import com.streaminsightsanalyserapplication.model.User;
import com.streaminsightsanalyserapplication.repository.MovieDAO;
import com.streaminsightsanalyserapplication.repository.MovieDAOImpl;
import com.streaminsightsanalyserapplication.repository.MovieReviewDAO;
import com.streaminsightsanalyserapplication.repository.MovieReviewDAOImpl;
import com.streaminsightsanalyserapplication.repository.UserDAO;
import com.streaminsightsanalyserapplication.repository.UserDAOImpl;

@Component
public class FunctionalTests {

	private static MovieReviewDAO movieReviewDAO;
	private static MovieDAO movieDAO;
	private static UserDAO userDAO;

	@BeforeAll
	public static void setUp() {
		Properties properties = new Properties();
		try (InputStream inputStream = StreamInsightsAnalyserApplication.class.getClassLoader()
				.getResourceAsStream("application.properties")) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String dbUrl = properties.getProperty("db.url") + properties.getProperty("db.database");
		String dbUser = properties.getProperty("db.username");
		String dbPassword = properties.getProperty("db.password");

		movieReviewDAO = new MovieReviewDAOImpl(dbUrl, dbUser, dbPassword);
		movieDAO = new MovieDAOImpl(dbUrl, dbUser, dbPassword);
		userDAO = new UserDAOImpl(dbUrl, dbUser, dbPassword);
	}

	@BeforeEach
	void clearDatabase() {
		try {
			movieReviewDAO.deleteAllMovieReviews();
			movieDAO.deleteAllMovies();
			userDAO.deleteAllUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testAddMovie() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");

		movieDAO.addMovie(movie);
		if (movieDAO.getAllMovies() != null) {
			Movie retrievedMovie = movieDAO.getAllMovies().get(0);
			try {
				yakshaAssert(currentTest(),
						retrievedMovie != null && retrievedMovie.getName().equals(movie.getName())
								&& retrievedMovie.getCertificate().equals(movie.getCertificate()) ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}

		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetMovieById() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");

		movieDAO.addMovie(movie);
		if (movieDAO.getAllMovies() != null) {
			movie = movieDAO.getAllMovies().get(0);
			try {
				yakshaAssert(currentTest(),
						movie != null && movie.getName().equals("Movie 1") && movie.getCertificate().equals("PG-13")
								? true
								: false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}

		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetMostWatchedMovies() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");

		movieDAO.addMovie(movie);
		userDAO.addUser(new User("User 1", 30, "Male"));

		if (userDAO.getAllUsers() != null && movieDAO.getAllMovies() != null
				&& movieDAO.getMostWatchedMovies() != null) {
			movieReviewDAO.addMovieReview(new MovieReview(movieDAO.getAllMovies().get(0).getId(),
					userDAO.getAllUsers().get(0).getId(), "Movie review", 4));

			List<Movie> mostWatchedMovies = movieDAO.getMostWatchedMovies();
			try {
				yakshaAssert(currentTest(), mostWatchedMovies != null && mostWatchedMovies.size() == 1 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSortMoviesByGenreCount() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");

		movieDAO.addMovie(movie);
		userDAO.addUser(new User("User 1", 30, "Male"));

		if (userDAO.getAllUsers() != null && movieDAO.getAllMovies() != null
				&& movieDAO.getMostWatchedMovies() != null) {
			movieReviewDAO.addMovieReview(new MovieReview(movieDAO.getAllMovies().get(0).getId(),
					userDAO.getAllUsers().get(0).getId(), "Movie review", 4));
			List<Movie> sortedMovies = movieDAO.sortMoviesByGenreCount();
			try {
				yakshaAssert(currentTest(), sortedMovies != null && sortedMovies.size() == 1 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetTopMoviesByAgeRange() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");

		movieDAO.addMovie(movie);
		userDAO.addUser(new User("User 1", 30, "Male"));
		if (userDAO.getAllUsers() != null && movieDAO.getAllMovies() != null
				&& movieDAO.getMostWatchedMovies() != null) {
			movieReviewDAO.addMovieReview(new MovieReview(movieDAO.getAllMovies().get(0).getId(),
					userDAO.getAllUsers().get(0).getId(), "Movie review", 4));
			List<Movie> topMovies = movieDAO.getTopMoviesByAgeRange(25, 30, 10);
			try {
				yakshaAssert(currentTest(), topMovies != null && topMovies.size() == 1 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchMoviesByDirectorAndRating() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");

		movieDAO.addMovie(movie);
		userDAO.addUser(new User("User 1", 30, "Male"));

		if (userDAO.getAllUsers() != null && movieDAO.getAllMovies() != null
				&& movieDAO.getMostWatchedMovies() != null) {
			movieReviewDAO.addMovieReview(new MovieReview(movieDAO.getAllMovies().get(0).getId(),
					userDAO.getAllUsers().get(0).getId(), "Movie review", 4));
			List<Movie> movies = movieDAO.searchMoviesByDirectorAndRating("Director 1", 4);
			try {
				yakshaAssert(currentTest(), movies != null ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetLowestRatedMovieByActor() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");

		movieDAO.addMovie(movie);
		userDAO.addUser(new User("User 1", 30, "Male"));

		if (userDAO.getAllUsers() != null && movieDAO.getAllMovies() != null
				&& movieDAO.getMostWatchedMovies() != null) {
			movieReviewDAO.addMovieReview(new MovieReview(movieDAO.getAllMovies().get(0).getId(),
					userDAO.getAllUsers().get(0).getId(), "Movie review", 4));
			Movie lowestRatedMovie = movieDAO.getLowestRatedMovieByActor("Actor 1");
			try {
				yakshaAssert(currentTest(), lowestRatedMovie != null ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchMoviesByGenreAndLength() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");

		movieDAO.addMovie(movie);
		userDAO.addUser(new User("User 1", 30, "Male"));

		if (userDAO.getAllUsers() != null && movieDAO.getAllMovies() != null
				&& movieDAO.getMostWatchedMovies() != null) {
			movieReviewDAO.addMovieReview(new MovieReview(movieDAO.getAllMovies().get(0).getId(),
					userDAO.getAllUsers().get(0).getId(), "Movie review", 4));
			List<Movie> movies = movieDAO.searchMoviesByGenreAndLength("Action", 150);
			try {
				yakshaAssert(currentTest(), movies != null ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateMovie() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");
		movieDAO.addMovie(movie);
		if (movieDAO.getAllMovies() != null) {
			movie = movieDAO.getAllMovies().get(0);
			movie.setName("Updated Movie 1");
			movie.setLength(150);
			movieDAO.updateMovie(movie);
			movie = movieDAO.getAllMovies().get(0);
			try {
				yakshaAssert(currentTest(),
						movie != null && movie.getName().equals("Updated Movie 1") && movie.getLength() == 150 ? true
								: false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteMovie() throws IOException {
		Movie movie = new Movie();
		movie.setId(1);
		movie.setName("Movie 1");
		movie.setGenre("Action");
		movie.setDirector("Director 1");
		movie.setStarCast("Actor 1, Actor 2");
		movie.setLength(120);
		movie.setCertificate("PG-13");
		movieDAO.addMovie(movie);
		if (movieDAO.getAllMovies() != null) {
			movie = movieDAO.getAllMovies().get(0);
			movieDAO.deleteMovie(movie.getId());
			List<Movie> movies = movieDAO.getAllMovies();
			try {
				yakshaAssert(currentTest(), movies != null && movies.size() == 0 ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteAllMovies() throws IOException {
		Movie movie1 = new Movie();
		movie1.setId(1);
		movie1.setName("Movie 1");
		movie1.setGenre("Action");
		movie1.setDirector("Director 1");
		movie1.setStarCast("Actor 1, Actor 2");
		movie1.setLength(120);
		movie1.setCertificate("PG-13");

		Movie movie2 = new Movie();
		movie2.setId(2);
		movie2.setName("Movie 2");
		movie2.setGenre("Drama");
		movie2.setDirector("Director 2");
		movie2.setStarCast("Actor 3, Actor 4");
		movie2.setLength(130);
		movie2.setCertificate("R");

		movieDAO.addMovie(movie1);
		movieDAO.addMovie(movie2);

		movieDAO.deleteAllMovies();

		if (movieDAO.getAllMovies() != null) {
			List<Movie> allMovies = movieDAO.getAllMovies();
			try {
				yakshaAssert(currentTest(), allMovies.isEmpty() ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllMovies() throws IOException {
		Movie movie1 = new Movie();
		movie1.setId(1);
		movie1.setName("Movie 1");
		movie1.setGenre("Action");
		movie1.setDirector("Director 1");
		movie1.setStarCast("Actor 1, Actor 2");
		movie1.setLength(120);
		movie1.setCertificate("PG-13");

		Movie movie2 = new Movie();
		movie2.setId(2);
		movie2.setName("Movie 2");
		movie2.setGenre("Drama");
		movie2.setDirector("Director 2");
		movie2.setStarCast("Actor 3, Actor 4");
		movie2.setLength(130);
		movie2.setCertificate("R");

		movieDAO.addMovie(movie1);
		movieDAO.addMovie(movie2);

		if (movieDAO.getAllMovies() != null) {
			List<Movie> allMovies = movieDAO.getAllMovies();
			try {
				yakshaAssert(currentTest(), !allMovies.isEmpty() ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetUserById() throws IOException {
		User user = new User();
		user.setId(1);
		user.setName("User 1");
		user.setAge(25);
		user.setGender("Male");

		userDAO.addUser(user);

		if (userDAO.getAllUsers() != null) {
			user = userDAO.getUserById(userDAO.getAllUsers().get(0).getId());
			try {
				yakshaAssert(currentTest(),
						user != null && user.getName().equals("User 1") && user.getAge() == 25 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testAddUser() throws IOException {
		User user = new User();
		user.setId(1);
		user.setName("User 1");
		user.setAge(25);
		user.setGender("Male");

		userDAO.addUser(user);

		if (userDAO.getAllUsers() != null) {
			user = userDAO.getAllUsers().get(0);
			try {
				yakshaAssert(currentTest(),
						user != null && user.getName().equals("User 1") && user.getAge() == 25 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateUser() throws IOException {
		User user = new User();
		user.setId(1);
		user.setName("User 1");
		user.setAge(25);
		user.setGender("Male");

		userDAO.addUser(user);

		if (userDAO.getAllUsers() != null) {
			user = userDAO.getAllUsers().get(0);
			user.setName("Updated User 1");
			user.setAge(30);
			user.setGender("Female");
			userDAO.updateUser(user);
			user = userDAO.getUserById(userDAO.getAllUsers().get(0).getId());
			try {
				yakshaAssert(currentTest(),
						user != null && user.getName().equals("Updated User 1") && user.getAge() == 30 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteUser() throws IOException {
		User user = new User();
		user.setId(1);
		user.setName("User 1");
		user.setAge(25);
		user.setGender("Male");

		userDAO.addUser(user);

		if (userDAO.getAllUsers() != null) {
			userDAO.deleteUser(userDAO.getAllUsers().get(0).getId());
			try {
				yakshaAssert(currentTest(), userDAO.getAllUsers().isEmpty() ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteAllUsers() throws IOException {
		User user1 = new User();
		user1.setId(1);
		user1.setName("User 1");
		user1.setAge(25);
		user1.setGender("Male");
		User user2 = new User();
		user2.setId(2);
		user2.setName("User 2");
		user2.setAge(30);
		user2.setGender("Female");
		userDAO.addUser(user1);
		userDAO.addUser(user2);
		if (userDAO.getAllUsers() != null) {
			userDAO.deleteAllUsers();
			List<User> allUsers = userDAO.getAllUsers();
			try {
				yakshaAssert(currentTest(), allUsers.isEmpty() ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllUsers() throws IOException {
		User user1 = new User();
		user1.setId(1);
		user1.setName("User 1");
		user1.setAge(25);
		user1.setGender("Male");

		User user2 = new User();
		user2.setId(2);
		user2.setName("User 2");
		user2.setAge(30);
		user2.setGender("Female");

		userDAO.addUser(user1);
		userDAO.addUser(user2);

		if (userDAO.getAllUsers() != null) {
			List<User> allUsers = userDAO.getAllUsers();
			try {
				yakshaAssert(currentTest(), !allUsers.isEmpty() && allUsers.size() == 2 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testAddMovieReview() throws IOException {
		MovieReview movieReview = new MovieReview();
		movieReview.setId(1);
		movieReview.setMovieId(1);
		movieReview.setUserId(1);
		movieReview.setReview("Great movie!");
		movieReview.setRating(5);
		movieReviewDAO.addMovieReview(movieReview);
		if (movieReviewDAO.getAllMovieReviews() != null) {
			movieReview = movieReviewDAO.getAllMovieReviews().get(0);
			try {
				yakshaAssert(currentTest(), movieReview != null && movieReview.getRating() == 5
						&& movieReview.getReview().equals("Great movie!") ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateMovieReview() throws IOException {
		MovieReview movieReview = new MovieReview();
		movieReview.setId(1);
		movieReview.setMovieId(1);
		movieReview.setUserId(1);
		movieReview.setReview("Great movie!");
		movieReview.setRating(5);

		movieReviewDAO.addMovieReview(movieReview);

		if (movieReviewDAO.getAllMovieReviews() != null) {
			movieReview = movieReviewDAO.getAllMovieReviews().get(0);
			movieReview.setReview("Updated review");
			movieReview.setRating(4);
			movieReviewDAO.updateMovieReview(movieReview);
			movieReview = movieReviewDAO.getMovieReviewById(movieReviewDAO.getAllMovieReviews().get(0).getId());
			try {
				yakshaAssert(currentTest(), movieReview != null && movieReview.getRating() == 4
						&& movieReview.getReview().equals("Updated review") ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteMovieReview() throws IOException {
		MovieReview movieReview = new MovieReview();
		movieReview.setId(1);
		movieReview.setMovieId(1);
		movieReview.setUserId(1);
		movieReview.setReview("Great movie!");
		movieReview.setRating(5);

		movieReviewDAO.addMovieReview(movieReview);

		if (movieReviewDAO.getAllMovieReviews() != null) {
			movieReviewDAO.deleteMovieReview(movieReviewDAO.getAllMovieReviews().get(0).getId());
			try {
				yakshaAssert(currentTest(), movieReviewDAO.getAllMovieReviews().isEmpty() ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllMovieReviews() throws IOException {
		MovieReview movieReview1 = new MovieReview();
		movieReview1.setId(1);
		movieReview1.setMovieId(1);
		movieReview1.setUserId(1);
		movieReview1.setReview("Great movie!");
		movieReview1.setRating(5);

		MovieReview movieReview2 = new MovieReview();
		movieReview2.setId(2);
		movieReview2.setMovieId(2);
		movieReview2.setUserId(2);
		movieReview2.setReview("Awesome film!");
		movieReview2.setRating(4);

		movieReviewDAO.addMovieReview(movieReview1);
		movieReviewDAO.addMovieReview(movieReview2);

		if (movieReviewDAO.getAllMovieReviews() != null) {
			List<MovieReview> allMovieReviews = movieReviewDAO.getAllMovieReviews();
			try {
				yakshaAssert(currentTest(), !allMovieReviews.isEmpty() && allMovieReviews.size() == 2 ? true : false,
						businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetMovieReviewById() throws IOException {
		MovieReview movieReview = new MovieReview();
		movieReview.setId(1);
		movieReview.setMovieId(1);
		movieReview.setUserId(1);
		movieReview.setReview("Great movie!");
		movieReview.setRating(5);

		movieReviewDAO.addMovieReview(movieReview);

		if (movieReviewDAO.getAllMovieReviews() != null) {
			movieReview = movieReviewDAO.getMovieReviewById(movieReviewDAO.getAllMovieReviews().get(0).getId());
			try {
				yakshaAssert(currentTest(), movieReview != null && movieReview.getRating() == 5
						&& movieReview.getReview().equals("Great movie!") ? true : false, businessTestFile);
			} catch (Exception e) {
				yakshaAssert(currentTest(), false, businessTestFile);
			}
		} else {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}
}
