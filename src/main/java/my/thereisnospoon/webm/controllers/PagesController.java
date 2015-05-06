package my.thereisnospoon.webm.controllers;

import my.thereisnospoon.webm.entities.repos.WebMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PagesController {

	private static final int VIDEOS_PER_ROW = 4;
	private static final int INITIAL_ROWS = 5;

	@Autowired
	private WebMRepository webMRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHomePage(Model model) {

		model.addAttribute("videos_in_row", VIDEOS_PER_ROW);
		model.addAttribute("title", "webm-haus");
		model.addAttribute("webms", webMRepository.getSliceWebMs(
				new PageRequest(0, VIDEOS_PER_ROW * INITIAL_ROWS, Sort.Direction.DESC, "postedWhen")).getContent());

		return "page_with_videos";
	}

	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public String uploadPage(Model model) {

		model.addAttribute("title", "WebM upload");
		return "upload";
	}
}
