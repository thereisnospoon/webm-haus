package my.thereisnospoon.webm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Document(collection = "webms")
public class WebMPost {

	@Id
	private String id;

	private String name;
	private String description;
	private BigInteger size;
	private int duration;
	private String previewId;
	private String fileId;
	private ZonedDateTime postedWhen;
	private User postedBy;
	private Set<String> tags;
	private Long likesCounter;
	private Long viewsCounter;
	private List<Comment> topComemnts;
	private List<CommentsChunk> additionalComments;
}
