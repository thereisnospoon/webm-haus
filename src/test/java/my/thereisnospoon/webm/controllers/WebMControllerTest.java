package my.thereisnospoon.webm.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/webm-config.xml"})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class WebMControllerTest {

	@Autowired
	private WebMController webMController;

	@Test
	public void testGetWebM() {

		System.out.println(webMController);
	}
}
