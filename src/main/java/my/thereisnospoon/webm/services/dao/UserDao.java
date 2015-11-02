package my.thereisnospoon.webm.services.dao;

import my.thereisnospoon.webm.entities.User;

public interface UserDao {

	void saveToDb(User user);

	User findUserByUsername(String username);

	boolean exists(String username);

	void delete(String username);
}
