package my.thereisnospoon.webm.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.LocalDate;

@Value.Immutable
@JsonSerialize(as = ImmutableVideo.class)
@JsonDeserialize(as = ImmutableVideo.class)
public interface Video {

	String getId();
	String getMd5Hash();
	Integer getDuration();
	String getThumbnailId();
	Long getSize();
	LocalDate getUploadDate();
}