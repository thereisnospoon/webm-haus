package my.thereisnospoon.webm.repositories;

import my.thereisnospoon.webm.AbstractIntegrationTest;
import my.thereisnospoon.webm.vo.User;
import my.thereisnospoon.webm.vo.Video;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class VideoRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	private VideoRepository testedInstance;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setUp() {

		deleteDataIfExists();
		Video video = createTestVideo();
		testedInstance.save(video);
	}

	@After
	public void cleanUp() {
		deleteDataIfExists();
	}

	private void deleteDataIfExists() {

		if (testedInstance.exists(TEST_VIDEO_ID)) {
			testedInstance.delete(TEST_VIDEO_ID);
		}

		if (userRepository.exists(TEST_USERNAME)) {
			userRepository.delete(TEST_USERNAME);
		}
	}

	@Test
	public void shouldRetrieveSavedVideo() {

		Video video = testedInstance.findOne(TEST_VIDEO_ID);
		assertEquals(TEST_VIDEO_NUMERIC_DATA, video.getSize());
	}

	@Test
	public void shouldDeleteVideo() {

		assertTrue(testedInstance.exists(TEST_VIDEO_ID));
		testedInstance.delete(TEST_VIDEO_ID);
		assertFalse(testedInstance.exists(TEST_VIDEO_ID));
	}
}