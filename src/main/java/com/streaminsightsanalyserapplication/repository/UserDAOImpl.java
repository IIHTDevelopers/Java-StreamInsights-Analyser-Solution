package com.streaminsightsanalyserapplication.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.streaminsightsanalyserapplication.model.User;

public class UserDAOImpl implements UserDAO {

	private final String dbUrl;
	private final String dbUser;
	private final String dbPassword;

	public UserDAOImpl(String dbUrl, String dbUser, String dbPassword) {
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
	}

	@Override
	public void addUser(User user) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO user (name, age, gender) VALUES (?, ?, ?)")) {
			statement.setString(1, user.getName());
			statement.setInt(2, user.getAge());
			statement.setString(3, user.getGender());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUser(User user) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection
						.prepareStatement("UPDATE user SET name = ?, age = ?, gender = ? WHERE id = ?")) {
			statement.setString(1, user.getName());
			statement.setInt(2, user.getAge());
			statement.setString(3, user.getGender());
			statement.setInt(4, user.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getUserById(int id) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {
			statement.setInt(1, id);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("name"));
				user.setAge(resultSet.getInt("age"));
				user.setGender(resultSet.getString("gender"));

				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void deleteUser(int id) {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("DELETE FROM user WHERE id = ?")) {
			statement.setInt(1, id);

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAllUsers() {
		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				PreparedStatement statement = connection.prepareStatement("DELETE FROM user")) {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				Statement statement = connection.createStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT * FROM user");

			while (resultSet.next()) {
				User user = new User();
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("name"));
				user.setAge(resultSet.getInt("age"));
				user.setGender(resultSet.getString("gender"));

				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}
}
