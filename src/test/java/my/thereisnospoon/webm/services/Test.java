package my.thereisnospoon.webm.services;

import my.thereisnospoon.webm.vo.User;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webm-config.xml")
@ActiveProfiles("windows")
public class Test {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	@org.junit.Test
	public void test2() {
		entityManager.persist(new User("asdads", "asdasd", "asdasd", "asdasd"));
		assertNotNull(entityManager.find(User.class, "asdads"));
	}
}
