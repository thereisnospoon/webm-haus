package my.thereisnospoon.webm.vo;

import lombok.Value;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users")
@Value
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
