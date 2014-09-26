package my.thereisnospoon.webm.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

@ContextConfiguration(locations = {"file:src/main/java/my/thereisnospoon/webm/webm-config.xml"})
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
