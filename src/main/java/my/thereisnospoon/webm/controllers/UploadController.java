package my.thereisnospoon.webm.controllers;

import com.mongodb.gridfs.GridFSFile;
import my.thereisnospoon.webm.entities.WebMPost;
import my.thereisnospoon.webm.entities.repos.WebMRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Controller
@RequestMapping("/upload")
public class UploadController {

	private static final Logger log = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private WebMRepository webMRepository;

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@RequestMapping(method = RequestMethod.GET)
	public String uploadPage() {
		return "upload";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String uploadFile(@RequestParam MultipartFile file) throws IOException {

		log.debug("Starting file {} uploading", file.getName());

		GridFSFile fsFile = gridFsTemplate.store(file.getInputStream(), "file.webm");
		return "{\"webMId\": \"" + fsFile.getId() + "\"}";
	}

	@RequestMapping(value = "meta", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public WebMPost uploadMetaData(WebMPost webMPost,
								   @RequestParam(required = false) String tagsString,
								   @RequestParam Date date,
								   @RequestParam int timeZoneOffset) {

		log.debug("Posted when = {}; timeZoneOffset = {}", date, timeZoneOffset);

		webMPost.setPostedWhen(ZonedDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.of("+" + (-timeZoneOffset/60))));

		log.debug("Posted when: {}", webMPost.getPostedWhen());

		return webMRepository.save(webMPost);
	}
}
