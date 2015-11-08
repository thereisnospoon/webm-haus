package my.thereisnospoon.webm.controllers;

import com.mongodb.gridfs.GridFSDBFile;
import my.thereisnospoon.webm.services.gridfs.GridFsService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/webm")
public class WebMController {

	private static final Logger log = LoggerFactory.getLogger(WebMController.class);

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private static Pattern DIGITS_PATTERN = Pattern.compile("\\d+");

	@Autowired
	private GridFsService gridFsService;

	@RequestMapping(value = "/preview/{previewId}", method = RequestMethod.GET)
	public void getPreview(HttpServletResponse response, @PathVariable String previewId) throws IOException {

		GridFSDBFile previewFile = gridFsService.loadDataFromDB(previewId);

		if (previewFile == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType("image/png");
		response.setContentLength((int) previewFile.getLength());
		response.setHeader("ETag", previewFile.getMD5());
		IOUtils.copy(previewFile.getInputStream(), response.getOutputStream());
	}

	@RequestMapping(value = "/data/{fileId}", method = RequestMethod.GET)
	public void getWebMData(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId)
			throws Exception {

		GridFSDBFile file = gridFsService.loadDataFromDB(fileId);

		if (file == null) {

			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		long fileLength = file.getLength();

		response.setContentType("video/webm");
		response.setHeader("Accept-Ranges", "bytes");
		response.setHeader("ETag", file.getMD5());

		InputStream inputStream = file.getInputStream();
		OutputStream outputStream = response.getOutputStream();

		if (StringUtils.isEmpty(request.getHeader("Range"))) {

			response.setHeader("Content-Length", "" + fileLength);
			IOUtils.copy(inputStream, outputStream);
		} else {

			long[] range = parseRange(request.getHeader("Range"), fileLength);

			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			response.setHeader("Content-Range", "bytes " + range[0] + "-" + range[1] + "/" + fileLength);
			response.setHeader("Content-Length", "" + (range[1] - range[0] + 1));

			writeResponseBody(inputStream, outputStream, range);
		}
	}

	private long[] parseRange(String range, long fileLength) {

		long[] bounds = new long[2];
		Matcher matcher = DIGITS_PATTERN.matcher(range);
		if (matcher.find()) {
			bounds[0] = Long.parseLong(matcher.group());
		}
		if (matcher.find()) {
			bounds[1] = Long.parseLong(matcher.group());
		} else {
			bounds[1] = fileLength - 1;
		}
		return bounds;
	}

	private void writeResponseBody(InputStream inputStream, OutputStream outputStream, long[] range) throws Exception {

		inputStream.skip(range[0]);
		long bytesToWrite = range[1] - range[0] + 1;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

		while (bytesToWrite > 0) {

			int read;
			if (bytesToWrite > buffer.length) {
				read = inputStream.read(buffer);
			} else {
				read = inputStream.read(buffer, 0, (int) bytesToWrite);
			}

			bytesToWrite -= read;
			outputStream.write(buffer, 0, read);
		}
		inputStream.close();
	}
}
