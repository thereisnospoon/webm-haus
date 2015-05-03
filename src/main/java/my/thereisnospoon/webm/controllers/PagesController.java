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

	@Autowired
	private WebMRepository webMRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHomePage(Model model) {

		model.addAttribute("title", "webm-haus");
		model.addAttribute("webms", webMRepository.getSliceWebMs(new PageRequest(0, 15)).getContent());

		return "page_with_videos";
	}
}
