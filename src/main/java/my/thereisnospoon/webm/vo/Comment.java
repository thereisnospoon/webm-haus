package my.thereisnospoon.webm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("comments")
public class Comment {

	@Data
	@Builder
	@PrimaryKeyClass
	public static class Key implements Serializable {


		@PrimaryKeyColumn(name = "ts", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
		private Date ts;

		@PrimaryKeyColumn(name = "videoid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
		private String videoId;
	}

	@PrimaryKey
	private Key key;

	private String username;
	private String text;

	private long likesCounter;

}
