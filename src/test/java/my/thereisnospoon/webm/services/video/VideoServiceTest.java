package my.thereisnospoon.webm.services.video;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/webm-config.xml")
@ActiveProfiles("windows")
public class VideoServiceTest {

	@Autowired
	private VideoService videoService;

	@Test
	public void testGetDuration() throws Exception {

	}
}