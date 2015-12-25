package my.thereisnospoon.webm.repositories;

import my.thereisnospoon.webm.vo.User;
import my.thereisnospoon.webm.vo.Video;
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

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webm-config.xml")
@ActiveProfiles("windows")
@Transactional
@Commit
public class VideoRepositoryTest {

	private static final String STRING_DATA = "1";
	private static final Long LONG_DATA = 1L;
	private static final String TEST_USERNAME = "video_test_username";
	private static final String TEST_USER_EMAIL = "test_video@mail.com";

	@Autowired
	private VideoRepository testedInstance;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() {

		deleteDataIfExists();

		User user = User.builder()
				.username(TEST_USERNAME)
				.email(TEST_USER_EMAIL)
				.build();

		user = userRepository.save(user);

		Video video = Video.builder()
				.id(STRING_DATA)
				.md5Hash(STRING_DATA)
				.duration(LONG_DATA.intValue())
				.size(LONG_DATA)
				.thumbnailId(STRING_DATA)
				.uploadDate(LocalDate.now())
				.user(user)
				.build();

		testedInstance.save(video);
	}

	@After
	public void cleanUp() {
		deleteDataIfExists();
	}

	private void deleteDataIfExists() {

		if (userRepository.exists(TEST_USERNAME)) {
			userRepository.delete(TEST_USERNAME);
		}

		if (testedInstance.exists(STRING_DATA)) {
			testedInstance.delete(STRING_DATA);
		}
	}

	@Test
	public void shouldRetrieveSavedVideo() {

		Video video = testedInstance.findOne(STRING_DATA);
		assertEquals(LONG_DATA, video.getSize());
	}

	@Test
	public void shouldDeleteVideo() {

		assertTrue(testedInstance.exists(STRING_DATA));
		testedInstance.delete(STRING_DATA);
		assertFalse(testedInstance.exists(STRING_DATA));
	}

	@Test
	public void shouldHaveReferenceToUser() {

		Video video = testedInstance.findOne(STRING_DATA);
		assertEquals(TEST_USER_EMAIL, video.getUser().getEmail());
	}
}