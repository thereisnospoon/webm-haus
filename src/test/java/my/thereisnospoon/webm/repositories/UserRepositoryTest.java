package my.thereisnospoon.webm.repositories;

import my.thereisnospoon.webm.vo.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webm-config.xml")
@ActiveProfiles("windows")
@Transactional
@Commit
public class UserRepositoryTest {

	private static final String TEST_USER_EMAIL = "test@mail.com";
	private static final String TEST_USERNAME = "test_user";

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() {
		userRepository.save(new User(TEST_USERNAME, TEST_USER_EMAIL, null, null));
	}

	@After
	public void cleanUp() {
		if (userRepository.exists(TEST_USER_EMAIL)) {
			userRepository.delete(TEST_USER_EMAIL);
		}
	}

	@Test
	public void shouldRetrieveTestUser() {

		User testUser = userRepository.findOne(TEST_USER_EMAIL);
		assertEquals(TEST_USERNAME, testUser.getUsername());
	}

	@Test
	public void shouldDeleteUser() {

		assertTrue(userRepository.exists(TEST_USER_EMAIL));
		userRepository.delete(TEST_USER_EMAIL);
		assertFalse(userRepository.exists(TEST_USER_EMAIL));
	}
}