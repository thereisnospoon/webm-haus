package my.thereisnospoon.webm.controllers;

import com.mongodb.gridfs.GridFSFile;
import my.thereisnospoon.webm.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/upload")
public class UploadController {

	private static final Logger log = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private FileService fileService;

	@RequestMapping(method = RequestMethod.GET)
	public String uploadPage() {
		return "upload";
	}

	@RequestMapping(value = "file", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String uploadFile(@RequestParam MultipartFile file) throws IOException {

		log.debug("Starting file {} uploading", file.getName());

		GridFSFile fsFile = fileService.storeWebM(file.getInputStream(), "temp.webm");
		return "{\"webMId\": \"" + fsFile.getId() + "\"}";
	}
}
