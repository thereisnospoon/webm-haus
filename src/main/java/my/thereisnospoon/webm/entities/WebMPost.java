package my.thereisnospoon.webm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "webms")
public class WebMPost {

	@Id
	private String id;

	private String name;
	private String description;
	private String previewId;
	private String fileId;
	private Date postedWhen;
	private int timezoneOffset;
	private int duration;
	private User postedBy;
	private Set<String> tags = new HashSet<>();
	private long likesCounter;
	private long viewsCounter;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Date getPostedWhen() {
		return postedWhen;
	}

	public void setPostedWhen(Date postedWhen) {
		this.postedWhen = postedWhen;
	}

	public User getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(User postedBy) {
		this.postedBy = postedBy;
	}

	public Set<String> getTags() {
		return tags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getTimezoneOffset() {
		return timezoneOffset;
	}

	public void setTimezoneOffset(int timezoneOffset) {
		this.timezoneOffset = timezoneOffset;
	}

	public long getLikesCounter() {
		return likesCounter;
	}

	public void setLikesCounter(long likesCounter) {
		this.likesCounter = likesCounter;
	}

	public long getViewsCounter() {
		return viewsCounter;
	}

	public void setViewsCounter(long viewsCounter) {
		this.viewsCounter = viewsCounter;
	}

	@Override
	public String toString() {
		return "WebMPost{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", previewId='" + previewId + '\'' +
				", fileId='" + fileId + '\'' +
				", postedWhen=" + postedWhen +
				", timezoneOffset=" + timezoneOffset +
				", duration=" + duration +
				", postedBy=" + postedBy +
				", tags=" + tags +
				", likesCounter=" + likesCounter +
				", viewsCounter=" + viewsCounter +
				'}';
	}
}
