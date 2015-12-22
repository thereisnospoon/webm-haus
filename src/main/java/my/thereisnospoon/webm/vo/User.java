package my.thereisnospoon.webm.vo;

import lombok.Value;
import javax.annotation.Nullable;

@Value
public class User {

	private String username;
	private String email;
	@Nullable private String password;
	@Nullable private String encodedPassword;
}
