package my.thereisnospoon.webm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.annotation.Nullable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	private String username;
	private String email;

	@Transient
	@Nullable
	private String password;

	@Nullable
	private String encodedPassword;

	@ElementCollection
	@CollectionTable(name = "liked_videos", joinColumns = @JoinColumn(name = "username"))
	@Column(name = "video_id")
	@Singular
	private Set<String> likedVideos;

	@ElementCollection
	@CollectionTable(name = "liked_comments", joinColumns = @JoinColumn(name = "username"))
	@Column(name = "comment_id")
	@Singular
	private Set<String> likedComments;
}
