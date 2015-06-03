package my.thereisnospoon.webm.controllers;

import my.thereisnospoon.webm.entities.WebMPost;
import my.thereisnospoon.webm.entities.repos.WebMRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Controller
public class PagesController {

	private static final int VIDEOS_PER_ROW = 4;
	private static final int INITIAL_ROWS = 4;

	@Autowired
	private WebMRepository webMRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHomePage(Model model) {

		model.addAttribute("videos_in_row", VIDEOS_PER_ROW);
		model.addAttribute("title", "webm-haus");
		model.addAttribute("webms", webMRepository.getSliceWebMs(
				new PageRequest(0, VIDEOS_PER_ROW * INITIAL_ROWS, Sort.Direction.DESC, "postedWhen")).getContent());

		model.addAttribute("load_more_button", true);

		return "page_with_videos";
	}

	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public String uploadPage(Model model) {

		model.addAttribute("title", "WebM upload");
		return "upload";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(Model model, @RequestParam String keywords) {

		model.addAttribute("videos_in_row", VIDEOS_PER_ROW);
		model.addAttribute("title", "Search results");

		Set<WebMPost> searchResults = new HashSet<>();

		for (String keyword: keywords.split("\\W+")) {

			keyword = keyword.trim();
			if (StringUtils.isEmpty(keyword)) {
				continue;
			}

			Criteria searchCriteria = new Criteria();
			searchCriteria.orOperator(where("name").regex(".*" + keyword + ".*"), where("tags").in(keyword));

			searchResults.addAll(mongoTemplate.find(query(searchCriteria), WebMPost.class));
		}

		model.addAttribute("webms", new LinkedList<>(searchResults));

		return "page_with_videos";
	}
}
