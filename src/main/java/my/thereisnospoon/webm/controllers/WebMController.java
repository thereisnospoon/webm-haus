package my.thereisnospoon.webm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.gridfs.GridFsCriteria.where;

@Controller
@RequestMapping("/webm")
public class WebMController {

//	private static final Logger log = LoggerFactory.getLogger(WebMController.class);
//
//	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
//
//	private static Pattern DIGITS_PATTERN = Pattern.compile("\\d+");
//
//	@Autowired
//	private GridFsTemplate gridFsTemplate;
//
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private MongoTemplate mongoTemplate;
//
//	@RequestMapping(value = "/next/{afterWebmId}")
//	@ResponseBody
//	public List<WebMPost> getWebMsAfter(@RequestParam int webmQuantity, @PathVariable String afterWebmId) {
//
//		Query getDateQuery = query(where("_id").is(afterWebmId));
//		getDateQuery.fields().include("postedWhen");
//		Date postedWhen = mongoTemplate.findOne(getDateQuery, WebMPost.class).getPostedWhen();
//
//		Query findEarlierWebMs = query(where("postedWhen").lt(postedWhen)).limit(webmQuantity);
//		findEarlierWebMs.with(new Sort(new Sort.Order(Sort.Direction.DESC, "postedWhen")));
//
//		return  mongoTemplate.find(findEarlierWebMs, WebMPost.class);
//	}
//
//	@RequestMapping(value = "/preview/{previewId}", method = RequestMethod.GET)
//	public void getPreview(HttpServletResponse response, @PathVariable String previewId) throws IOException {
//
//		GridFSDBFile file = gridFsTemplate.findOne(query(where("_id").is(previewId)
//				.and("contentType").is("image")));
//
//		if (file == null) {
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			return;
//		}
//
//		response.setContentType("image/png");
//		response.setContentLength((int) file.getLength());
//		response.setHeader("ETag", file.getMD5());
//		IOUtils.copy(file.getInputStream(), response.getOutputStream());
//	}
//
//	@Secured(User.ROLE_USER)
//	@RequestMapping(value = "/like/{webmId}", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseVO toggleLike(@PathVariable String webmId, @AuthenticationPrincipal User user) {
//
//		log.debug("Toggling like for video with webmId = {} by user: {}", webmId, user);
//
//		WriteResult result;
//		String responseDesc;
//		if (user.getLikedVideos().contains(webmId)) {
//
//			result = mongoTemplate.updateFirst(query(where("_id").is(webmId)), new Update().inc("likesCounter", -1),
//					WebMPost.class);
//
//			user.getLikedVideos().remove(webmId);
//			responseDesc = "removed";
//		} else {
//
//			result = mongoTemplate.updateFirst(query(where("_id").is(webmId)), new Update().inc("likesCounter", 1),
//					WebMPost.class);
//
//			user.getLikedVideos().add(webmId);
//			responseDesc = "added";
//		}
//
//		if (result.getN() == 0) {
//			return new ResponseVO("failed", "Video not found", null);
//		}
//
//		userRepository.save(user);
//
//		return new ResponseVO("success", responseDesc, null);
//	}
//
//	@Secured(User.ROLE_USER)
//	@RequestMapping(value = "/isLiked/{webmId}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseVO isLikedByCurrentUser(@PathVariable String webmId, @AuthenticationPrincipal User user) {
//
//		log.debug("Checking whether current user: {} has ever liked webmId = {}", user.getUsername(), webmId);
//
//		if (user.getLikedVideos().contains(webmId)) {
//			return new ResponseVO("success", "true", null);
//		} else {
//			return new ResponseVO("success", "false", null);
//		}
//	}
//
//	@RequestMapping(value = "/data/{fileId}", method = RequestMethod.GET)
//	public void getWebMData(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId)
//			throws Exception {
//
//		GridFSDBFile file = gridFsTemplate.findOne(query(where("_id").is(fileId)
//				.and("contentType").is("video")));
//
//		if (file == null) {
//
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			return;
//		}
//
//		long fileLength = file.getLength();
//
//		response.setContentType("video/webm");
//		response.setHeader("Accept-Ranges", "bytes");
//		response.setHeader("ETag", file.getMD5());
//
//		InputStream inputStream = file.getInputStream();
//		OutputStream outputStream = response.getOutputStream();
//
//		if (StringUtils.isEmpty(request.getHeader("Range"))) {
//
//			response.setHeader("Content-Length", "" + fileLength);
//			IOUtils.copy(inputStream, outputStream);
//		} else {
//
//			long[] range = parseRange(request.getHeader("Range"), fileLength);
//
//			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
//			response.setHeader("Content-Range", "bytes " + range[0] + "-" + range[1] + "/" + fileLength);
//			response.setHeader("Content-Length", "" + (range[1] - range[0] + 1));
//
//			writeResponseBody(inputStream, outputStream, range);
//		}
//	}
//
//	private long[] parseRange(String range, long fileLength) {
//
//		long[] bounds = new long[2];
//		Matcher matcher = DIGITS_PATTERN.matcher(range);
//		if (matcher.find()) {
//			bounds[0] = Long.parseLong(matcher.group());
//		}
//		if (matcher.find()) {
//			bounds[1] = Long.parseLong(matcher.group());
//		} else {
//			bounds[1] = fileLength - 1;
//		}
//		return bounds;
//	}
//
//	private void writeResponseBody(InputStream inputStream, OutputStream outputStream, long[] range) throws Exception {
//
//		inputStream.skip(range[0]);
//		long bytesToWrite = range[1] - range[0] + 1;
//		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
//
//		while (bytesToWrite > 0) {
//
//			int read;
//			if (bytesToWrite > buffer.length) {
//				read = inputStream.read(buffer);
//			} else {
//				read = inputStream.read(buffer, 0, (int) bytesToWrite);
//			}
//
//			bytesToWrite -= read;
//			outputStream.write(buffer, 0, read);
//		}
//		inputStream.close();
//	}
}
