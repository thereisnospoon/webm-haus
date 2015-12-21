package my.thereisnospoon.webm.services.dao.impl;

import my.thereisnospoon.webm.vo.User;
import my.thereisnospoon.webm.services.dao.UserDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDaoImpl implements UserDao {

	@Resource(name = "redisTemplate")
	private HashOperations<String,String,User> redisHashOperations;

	@Value("${redis.collections.users}")
	private String redisCollectionNameForUsers;

	@Override
	public void saveToDb(User user) {

		if (exists(user.getUsername())) {
			throw new IllegalStateException(String.format("User '%s' already exists in DB", user.getUsername()));
		}
		redisHashOperations.put(redisCollectionNameForUsers, user.getUsername(), user);
	}

	@Override
	public User findUserByUsername(String username) {
		return redisHashOperations.get(redisCollectionNameForUsers, username);
	}

	@Override
	public boolean exists(String username) {
		return redisHashOperations.hasKey(redisCollectionNameForUsers, username);
	}

	@Override
	public void delete(String username) {

		if (!exists(username)) {
			throw new IllegalStateException(String.format("User '%s' doesn't exist", username));
		}
		redisHashOperations.delete(redisCollectionNameForUsers, username);
	}
}
