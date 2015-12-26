package my.thereisnospoon.webm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
	private Long id;
	private String username;
	private String text;
	private LocalDateTime timestamp;
	private long likesCounter;
	private String videoId;

	@OneToOne
	private Comment commentAnsweredOn;
}
