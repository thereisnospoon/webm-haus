package my.thereisnospoon.webm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Document(collection = "webms")
public class WebMPost {

	@Id
	private String id;

	private String name;
	private String description;
	private BigInteger size;
	private String previewId;
	private String fileId;
	private ZonedDateTime postedWhen;
	private User postedBy;
	private Set<String> tags;
	private Long likesCounter;
	private Long viewsCounter;
	private List<Comment> topComemnts = new LinkedList<>();
	private List<CommentsChunk> additionalComments = new LinkedList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigInteger getSize() {
		return size;
	}

	public void setSize(BigInteger size) {
		this.size = size;
	}

	public String getPreviewId() {
		return previewId;
	}

	public void setPreviewId(String previewId) {
		this.previewId = previewId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public ZonedDateTime getPostedWhen() {
		return postedWhen;
	}

	public void setPostedWhen(ZonedDateTime postedWhen) {
		this.postedWhen = postedWhen;
	}

	public User getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}
}
