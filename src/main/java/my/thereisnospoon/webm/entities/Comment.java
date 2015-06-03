package my.thereisnospoon.webm.entities;

import org.hibernate.validator.constraints.NotBlank;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * POJO that represents comment under WebM post
 */
@Document(collection = "comments")
public class Comment {

	@Id
	private String id;

	@NotBlank
	private String webmId;
	private String author;

	@NotBlank
	private String text;

	private Date postedWhen;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getPostedWhen() {
		return postedWhen;
	}

	public void setPostedWhen(Date postedWhen) {
		this.postedWhen = postedWhen;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWebmId() {
		return webmId;
	}

	public void setWebmId(String webmId) {
		this.webmId = webmId;
	}
	
	@JsonProperty
	public String getPrettyPostedWhen() {
		return new PrettyTime().format(postedWhen);
	}

	@Override
	public String toString() {
		return "Comment{" +
				"id='" + id + '\'' +
				", webmId='" + webmId + '\'' +
				", author='" + author + '\'' +
				", text='" + text + '\'' +
				", postedWhen=" + postedWhen +
				'}';
	}
}
