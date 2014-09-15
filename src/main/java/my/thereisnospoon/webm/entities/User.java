package my.thereisnospoon.webm.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;

@Document(collection = "users")
public class User {

	private String username;
	private String description;
	private ZonedDateTime createdWhen;
	private String password;
	private String avatarId;
	private List<String> roles;
}
