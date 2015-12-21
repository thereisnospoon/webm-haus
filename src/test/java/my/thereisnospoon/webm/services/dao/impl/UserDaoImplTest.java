package my.thereisnospoon.webm.services.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.thereisnospoon.webm.vo.ImmutableUser;
import my.thereisnospoon.webm.vo.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webm-config.xml")
@ActiveProfiles("windows")
public class UserDaoImplTest {

	@Autowired
	private UserDaoImpl testedInstance;
	@Autowired
	private ObjectMapper objectMapper;

	private User user;

	@Before
	public void setUp() {
		user = ImmutableUser.builder().email("123@gmail.com").username("testuser").build();
	}

	@After
	public void cleanUp() {

		if (testedInstance.exists(user.getUsername())) {
			testedInstance.delete(user.getUsername());
		}
	}

	@Test
	public void shouldSaveToDb() throws Exception {

		testedInstance.saveToDb(user);
		assertTrue(testedInstance.exists(user.getUsername()));
	}

	@Test
	public void shouldFindUserByUsername() throws Exception {

		testedInstance.saveToDb(user);
		User userFromDb = testedInstance.findUserByUsername(user.getUsername());
		assertEquals(user.getUsername(), userFromDb.getUsername());
	}

	@Test
	public void shouldDeleteUser() {

		testedInstance.saveToDb(user);
		testedInstance.delete(user.getUsername());
	}
}