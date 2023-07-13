package com.streaminsightsanalyserapplication.repository;

import java.util.List;

import com.streaminsightsanalyserapplication.model.User;

public interface UserDAO {
	void addUser(User user);

	void updateUser(User user);

	User getUserById(int id);

	void deleteUser(int id);

	void deleteAllUsers();

	List<User> getAllUsers();
}
