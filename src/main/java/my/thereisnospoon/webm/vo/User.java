package my.thereisnospoon.webm.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public interface User {

	String getUsername();
	String getEmail();
	@Nullable String getPassword();
	@Nullable String getEncodedPassword();
}
