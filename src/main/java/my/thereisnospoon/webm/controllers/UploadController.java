package my.thereisnospoon.webm.controllers;

import com.mongodb.gridfs.GridFSFile;
import my.thereisnospoon.webm.entities.User;
import my.thereisnospoon.webm.entities.WebMPost;
import my.thereisnospoon.webm.entities.repos.WebMRepository;
import my.thereisnospoon.webm.services.video.impl.VideoServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Controller
@RequestMapping("/upload")
public class UploadController {

//	private static final Logger log = LoggerFactory.getLogger(UploadController.class);
//
//	private static final String SESSION_ID_ATTRIBUTE = "SESSION_ID_ATTRIBUTE";
//	private static final String ANONYMOUS_USERNAME = "anonymous";
//
//	private WebMRepository webMRepository;
//	private GridFsTemplate gridFsTemplate;
//	private VideoServiceImpl ffmpegService;
//
//	@Autowired
//	public UploadController(WebMRepository webMRepository, GridFsTemplate gridFsTemplate,
//	                        VideoServiceImpl ffmpegService) {
//
//		this.webMRepository = webMRepository;
//		this.gridFsTemplate = gridFsTemplate;
//		this.ffmpegService = ffmpegService;
//	}
//
//	/**
//	 * Returns empty WebMPost as json in case when this file already exists in DB
//	 * @param file
//	 * @return
//	 * @throws IOException
//	 */
//	@RequestMapping(method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//	@ResponseBody
//	public WebMPost uploadFile(HttpServletRequest request, @RequestParam MultipartFile file) throws Exception {
//
//		log.debug("Starting file {} uploading", file.getOriginalFilename());
//
//		byte[] fileData = file.getBytes();
//		String md5Hash = DigestUtils.md5Hex(fileData);
//
//		log.debug("File's hash: {}", md5Hash);
//
//		if (gridFsTemplate.findOne(query(where("md5").is(md5Hash).and("contentType").is("video"))) != null) {
//
//			log.debug("This video file is already exists in DB");
//
//			return new WebMPost();
//		}
//
//		String pathToTempFile = ffmpegService.saveFileInTempFolder(fileData, md5Hash);
//		int videoDuration = ffmpegService.getVideoDuration(pathToTempFile);
//		byte[] thumbnail = ffmpegService.getThumbnail(pathToTempFile, md5Hash);
//
//		GridFSFile thumbnailFile = gridFsTemplate.store(new ByteArrayInputStream(thumbnail), "", "image");
//		GridFSFile storedFile = gridFsTemplate.store(new ByteArrayInputStream(fileData), "", "video");
//
//		boolean isTempFileDeleted = ffmpegService.deleteFileFromTempFolder(md5Hash);
//
//		log.debug("Is temp file deleted: {}", isTempFileDeleted);
//
//		WebMPost webMPost = new WebMPost();
//		webMPost.setFileId(storedFile.getId().toString());
//		webMPost.setPreviewId(thumbnailFile.getId().toString());
//		webMPost.setDuration(videoDuration);
//
//		// this attribute will be used to identify session during which webm was uploaded
//		request.getSession().setAttribute(SESSION_ID_ATTRIBUTE, request.getSession().getId());
//
//		return webMPost;
//	}
//
//	@RequestMapping(value = "/meta",
//			method = RequestMethod.POST,
//			consumes = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public WebMPost uploadMetaData(HttpServletRequest request, @RequestBody WebMPost webMPost,
//	                               @AuthenticationPrincipal User user) {
//
//		log.debug("Got metadata:\n{}", webMPost);
//
//		if (!Objects.equals(request.getSession().getAttribute(SESSION_ID_ATTRIBUTE), request.getSession().getId())) {
//			throw new IllegalStateException("Upload and meta-data submission sessions are different");
//		}
//
//		Set<String> tags = webMPost.getTags();
//		if (tags != null && !tags.isEmpty()) {
//			webMPost.setTags(new HashSet<>(Arrays.asList(tags.iterator().next().split("\\W+"))));
//		}
//
//		webMPost.setPostedWhen(new Date());
//		webMPost.setTimezoneOffset(ZonedDateTime.now().getOffset().getTotalSeconds());
//		webMPost.setPostedBy(user != null ? user.getUsername() : ANONYMOUS_USERNAME);
//
//		return webMRepository.save(webMPost);
//	}
}
