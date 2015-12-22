package my.thereisnospoon.webm.vo;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class Video {

	private String id;
	private String md5Hash;
	private Integer duration;
	private String thumbnailId;
	private Long size;
	private LocalDate uploadDate;
}