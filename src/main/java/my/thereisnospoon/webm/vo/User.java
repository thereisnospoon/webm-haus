package my.thereisnospoon.webm.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import javax.annotation.Nullable;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@PrimaryKey
	private String username;
	private String email;

	@Transient
	@Nullable
	private String password;

	@Nullable
	private String encodedPassword;
}
