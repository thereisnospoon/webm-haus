package my.thereisnospoon.webm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue
	private String id;
	private String username;
	private String text;
	private LocalDateTime timestamp;
	private long likesCounter;
	private long childCommentsQuantity;
	private String videoId;

	@Nullable
	private String commentAnsweredOnId;
}
