package my.thereisnospoon.webm.services.video;

import my.thereisnospoon.webm.AbstractIntegrationTest;
import my.thereisnospoon.webm.repositories.UserRepository;
import my.thereisnospoon.webm.repositories.VideoRepository;
import my.thereisnospoon.webm.services.gridfs.GridFsService;
import my.thereisnospoon.webm.vo.User;
import my.thereisnospoon.webm.vo.Video;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class VideoServiceTest extends AbstractIntegrationTest {

	private static final String TEST_VIDEO_NAME = "/test.webm";
	private static final String TEST_USER = "video_service_test_user";

	@Autowired
	private VideoService videoService;

	@Autowired
	private GridFsService gridFsService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VideoRepository videoRepository;

	private Video video;

	@Before
	public void setUp() throws Exception {

		video = videoService
				.processAndSaveVideo(IOUtils.toByteArray(VideoServiceTest.class.getResourceAsStream(TEST_VIDEO_NAME)));

		User user = User.builder()
				.username(TEST_USER)
				.email(TEST_USER)
				.build();

		userRepository.save(user);
	}

	@After
	public void cleanUp() {

		if (video != null) {
			gridFsService.deleteData(video.getId());
			gridFsService.deleteData(video.getThumbnailId());
			videoRepository.delete(video.getId());
		}

		if (userRepository.exists(TEST_USER)) {
			userRepository.delete(TEST_USER);
		}
	}

	@Test
	public void shouldStoreVideoData() {

		assertTrue(video.getSize() > 0);
		assertNotNull(video.getId());
		assertNotNull(video.getThumbnailId());
		assertTrue(video.getDuration() > 0);
	}

	@Test
	public void shouldLikeVideo() {

		videoService.likeVideo(video.getId(), TEST_USER);
		User user = userRepository.findOne(TEST_USER);
		Video retrievedVideo = videoRepository.findOne(video.getId());
		assertEquals(1L, retrievedVideo.getLikesCounter());
		assertTrue(user.getLikedVideos().contains(video.getId()));
	}

	@Test
	public void shouldRemoveLikeFromVideo() {

		videoService.likeVideo(video.getId(), TEST_USER);
		videoService.removeLikeFromVideo(video.getId(), TEST_USER);
		User user = userRepository.findOne(TEST_USER);
		Video retrievedVideo = videoRepository.findOne(video.getId());
		assertEquals(0L, retrievedVideo.getLikesCounter());
		assertFalse(user.getLikedVideos().contains(video.getId()));
	}

	@Test
	public void shouldIncrementViewsCounter() {

		videoService.incrementViewsCounter(video.getId());
		Video retrievedVideo = videoRepository.findOne(video.getId());
		assertEquals(1L, retrievedVideo.getViewsCounter());
	}
}