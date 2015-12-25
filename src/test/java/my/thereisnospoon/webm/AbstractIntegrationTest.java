package my.thereisnospoon.webm;

import my.thereisnospoon.webm.vo.User;
import my.thereisnospoon.webm.vo.Video;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webm-config.xml")
@ActiveProfiles("windows")
@Transactional
@Commit
public abstract class AbstractIntegrationTest {

	public static final String TEST_USERNAME = "test_username_1";
	public static final String TEST_USER_EMAIL = "test_username_1@mail.com";

	public static final String TEST_VIDEO_ID = "video_id";
	public static final String TEST_VIDEO_STRING_DATA = "v";
	public static final Long TEST_VIDEO_NUMERIC_DATA = 1L;

	public User createTestUser() {
		return User.builder()
				.email(TEST_USER_EMAIL)
				.username(TEST_USERNAME)
				.build();
	}

	public Video createTestVideo() {
		return Video.builder()
				.md5Hash(TEST_VIDEO_STRING_DATA)
				.duration(TEST_VIDEO_NUMERIC_DATA.intValue())
				.id(TEST_VIDEO_ID)
				.size(TEST_VIDEO_NUMERIC_DATA)
				.thumbnailId(TEST_VIDEO_STRING_DATA)
				.uploadDate(LocalDate.now())
				.build();
	}
}
