package my.thereisnospoon.webm.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;

import static org.junit.Assert.*;

public class FFMPEGServiceTest {

	private static final Logger log = LoggerFactory.getLogger(FFMPEGServiceTest.class);

	private static FFMPEGService ffmpegService;

	private static final String FFMPEG_PATH = "E:\\X-Files\\ffmpeg-20150425-git-a3110be-win64-static\\bin";
	private static final String THUMBNAIL_FOLDER = "E:\\Sources\\webm-haus\\build\\tmp";
	private static final String TEST_VIDEO_PATH = "src\\test\\resources\\test.webm";

	@BeforeClass
	public static void setUp() {
		Assume.assumeTrue(new File(FFMPEG_PATH).exists());
		ffmpegService = new FFMPEGService(FFMPEG_PATH, THUMBNAIL_FOLDER);
	}

	@Test
	public void testGetDuration() throws Exception {

		int duration = ffmpegService.getVideoDuration(TEST_VIDEO_PATH);
		log.debug("Duration is: {}", duration);
		assertEquals(duration, 106);
	}

	@Test
	public void testGetThumbnail() throws Exception {

		Instant start = Instant.now();
		String md5 = DigestUtils.md5Hex(new FileInputStream(TEST_VIDEO_PATH));

		log.debug("MD5 hash generated in {} ms", Duration.between(start, Instant.now()).toMillis());

		ffmpegService.getThumbnail(TEST_VIDEO_PATH, md5);
	}
}