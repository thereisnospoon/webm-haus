package my.thereisnospoon.webm.controllers;

import lombok.extern.slf4j.Slf4j;
import my.thereisnospoon.webm.services.video.VideoService;
import my.thereisnospoon.webm.services.video.exception.VideoAlreadyExistsException;
import my.thereisnospoon.webm.vo.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	private VideoService videoService;

	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Video uploadFile(@RequestParam MultipartFile file) throws Exception {

		log.debug("Starting file {} uploading", file.getOriginalFilename());

		byte[] fileData = file.getBytes();

		try {
			return videoService.processAndSaveVideo(fileData);
		} catch (VideoAlreadyExistsException e) {
			return null;
		}
	}
}
