package my.thereisnospoon.webm.services.video;

import my.thereisnospoon.webm.services.gridfs.GridFsService;
import my.thereisnospoon.webm.vo.Video;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webm-config.xml")
@ActiveProfiles("windows")
public class VideoServiceTest {

	private static final String TEST_VIDEO_NAME = "/test.webm";

	@Autowired
	private VideoService videoService;

	@Autowired
	private GridFsService gridFsService;

	private Video video;

	@Before
	public void setUp() throws Exception {

		video = videoService
				.processAndSaveVideo(IOUtils.toByteArray(VideoServiceTest.class.getResourceAsStream(TEST_VIDEO_NAME)));
	}

	@After
	public void cleanUp() {

		if (video != null) {
			gridFsService.deleteData(video.getId());
			gridFsService.deleteData(video.getThumbnailId());
		}
	}

	@Test
	public void testStoredVideoData() throws Exception {

		assertTrue(video.getSize() > 0);
		assertNotNull(video.getId());
		assertNotNull(video.getThumbnailId());
		assertTrue(video.getDuration() > 0);
	}
}