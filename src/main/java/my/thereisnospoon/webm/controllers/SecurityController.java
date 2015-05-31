package my.thereisnospoon.webm.controllers;

import my.thereisnospoon.webm.controllers.vo.ResponseVO;
import my.thereisnospoon.webm.entities.User;
import my.thereisnospoon.webm.entities.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/security")
public class SecurityController {

	private static final Logger log = LoggerFactory.getLogger(SecurityController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	private HttpHeaders getJsonHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return headers;
	}

	@RequestMapping(value = "/sign-up", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object createUser(@RequestBody @Valid User user, BindingResult bindingResult) {


		log.debug("Creating user: {}", user);
		log.debug("Binding result for new user: {}", bindingResult);

		if (bindingResult.hasFieldErrors()) {
			return new ResponseVO("failed", "Found errors during validation", bindingResult.getFieldErrors());
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(User.ROLE_USER).stream().collect(Collectors.toSet()));
		userRepository.insert(user);

		return user;
	}

	@RequestMapping(value = "/login-page", method = RequestMethod.GET)
	public ResponseEntity<String> loginPage() {
		return new ResponseEntity<>(getJsonHeaders(), HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(value = "/auth-fail", method = RequestMethod.GET)
	public ResponseEntity<ResponseVO> authFailure() {

		log.debug("Auth failed");

		return new ResponseEntity<>(new ResponseVO("failed", "authentication problem", null), getJsonHeaders(), HttpStatus.OK);
	}

	@RequestMapping(value = "/default-target", method = RequestMethod.GET)
	public ResponseEntity<User> defaultTarget() {

		log.debug("Getting default target");

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		log.debug("Current user is {}", user);

		return new ResponseEntity<>(user, getJsonHeaders(), HttpStatus.OK);
	}
}
