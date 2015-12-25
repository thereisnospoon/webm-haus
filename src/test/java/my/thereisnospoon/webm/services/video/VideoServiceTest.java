package my.thereisnospoon.webm.services.video;

import my.thereisnospoon.webm.repositories.UserRepository;
import my.thereisnospoon.webm.repositories.VideoRepository;
import my.thereisnospoon.webm.services.gridfs.GridFsService;
import my.thereisnospoon.webm.vo.User;
import my.thereisnospoon.webm.vo.Video;
import org.apache.commons.io.IOUtils;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webm-config.xml")
@ActiveProfiles("windows")
@Transactional
@Commit
public class VideoServiceTest {

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
		assertTrue(user.getLikedVideos().contains(video.getId()));
	}

	@Test
	public void shouldRemoveLikeFromVideo() {

		videoService.likeVideo(video.getId(), TEST_USER);
		videoService.removeLikeFromVideo(video.getId(), TEST_USER);
		User user = userRepository.findOne(TEST_USER);
		assertFalse(user.getLikedVideos().contains(video.getId()));
	}
}