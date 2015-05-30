package my.thereisnospoon.webm.aop;

import my.thereisnospoon.webm.entities.WebMPost;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Update;

@Aspect
@Component
public class Counters {

	private static final Logger log = LoggerFactory.getLogger(Counters.class);

	private static final Pattern RANGES_PATTERN = Pattern.compile("bytes=(\\d+)-.*");

	@Autowired
	private MongoTemplate mongoTemplate;

	@Before(value = "execution(* my.thereisnospoon.webm.controllers.WebMController.getWebMData(..))" +
			" && args(request, *, fileId))")
	public void viewsCounter(HttpServletRequest request, String fileId) {

		log.debug("Before getWebMData execution for fileId = {}", fileId);

		String range = request.getHeader("Range");

		log.debug("Requested range: {}", range);

		if (range == null || isRequestedFromStart(range)) {

			log.debug("Video's views counter will be incremented");

			mongoTemplate.updateFirst(query(where("fileId").is(fileId)), new Update().inc("viewsCounter", 1),
					WebMPost.class);
		}
	}

	private boolean isRequestedFromStart(String range) {

		Matcher matcher = RANGES_PATTERN.matcher(range);
		if (matcher.find()) {
			if (0L == Long.parseLong(matcher.group(1))) {
				return true;
			}
		}
		return false;
	}
}
