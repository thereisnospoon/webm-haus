package my.thereisnospoon.webm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "videos")
public class Video {

	@Id
	private String id;
	private String md5Hash;
	private Integer duration;
	private String thumbnailId;
	private Long size;
	private LocalDate uploadDate;

	private long viewsCounter;
	private long likesCounter;

	@OneToOne
	private User user;
}