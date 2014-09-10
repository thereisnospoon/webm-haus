package my.thereisnospoon.webm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Document
public class WebMPost {

	@Id
	private String id;

	private String name;
	private BigInteger size;
	private int duration;
	private String previewId;
	private String fileId;
	private ZonedDateTime postedWhen;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getSize() {
		return size;
	}

	public void setSize(BigInteger size) {
		this.size = size;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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
}
