package my.thereisnospoon.webm.entities.constraints;

import my.thereisnospoon.webm.entities.User;
import my.thereisnospoon.webm.entities.repos.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, User> {
	
	private static final Logger log = LoggerFactory.getLogger(UniqueUsernameValidator.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public void initialize(UniqueUsername constraintAnnotation) {

	}

	@Override
	public boolean isValid(User user, ConstraintValidatorContext context) {
		
		log.debug("Validating object: {}", user);
		
		if (user.getUsername() == null) {
			return true;
		}
		
		User userInDb = userRepository.findFirstByUsername(user.getUsername());
		if (userInDb == null) {
			return true;
		}
		
		if (userInDb.getId().equals(user.getId())) {
			return true;
		} else {
			return false;
		}
	}
}
