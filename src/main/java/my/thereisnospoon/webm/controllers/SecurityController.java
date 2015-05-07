package my.thereisnospoon.webm.controllers;

import my.thereisnospoon.webm.entities.User;
import my.thereisnospoon.webm.entities.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class SecurityController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object createUser(@RequestBody User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.insert(user);

		return user;
	}
}
