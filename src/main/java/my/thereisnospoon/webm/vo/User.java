package my.thereisnospoon.webm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private String username;

	@Id
	private String email;

	@Transient
	@Nullable
	private String password;

	@Nullable
	private String encodedPassword;
}
