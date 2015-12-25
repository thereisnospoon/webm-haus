package my.thereisnospoon.webm.repositories;

import my.thereisnospoon.webm.AbstractIntegrationTest;
import my.thereisnospoon.webm.vo.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() {

		if (userRepository.exists(TEST_USERNAME)) {
			userRepository.delete(TEST_USERNAME);
		}
		userRepository.save(createTestUser());
	}

	@After
	public void cleanUp() {
		if (userRepository.exists(TEST_USERNAME)) {
			userRepository.delete(TEST_USERNAME);
		}
	}

	@Test
	public void shouldRetrieveTestUser() {

		User testUser = userRepository.findOne(TEST_USERNAME);
		assertEquals(TEST_USERNAME, testUser.getUsername());
	}

	@Test
	public void shouldDeleteUser() {

		assertTrue(userRepository.exists(TEST_USERNAME));
		userRepository.delete(TEST_USERNAME);
		assertFalse(userRepository.exists(TEST_USERNAME));
	}
}