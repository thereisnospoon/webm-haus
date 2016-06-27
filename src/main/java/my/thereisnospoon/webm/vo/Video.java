package my.thereisnospoon.webm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Video {

	@PrimaryKey
	private String id;
	private String md5Hash;
	private Integer duration;
	private String thumbnailId;
	private Long size;
	private Date uploadDate;

	private long viewsCounter;
	private long likesCounter;
}