package my.thereisnospoon.webm.controllers;

import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/webm")
public class WebMController {

	private static final Logger log = LoggerFactory.getLogger(WebMController.class);
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	private static Pattern pattern = Pattern.compile("\\d+");

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@RequestMapping(value = "/{webmId}", method = RequestMethod.GET)
	public void getWebM(HttpServletRequest request, HttpServletResponse response, @PathVariable String webmId)
			throws Exception {

		GridFSDBFile file = gridFsTemplate.findOne(Query.query(GridFsCriteria.where("_id").is(webmId)));

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
		Matcher matcher = pattern.matcher(range);
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
	}
}
