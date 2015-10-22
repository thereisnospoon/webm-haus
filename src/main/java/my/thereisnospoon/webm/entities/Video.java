package my.thereisnospoon.webm.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.LocalDate;

@Value.Immutable
@JsonSerialize(as = ImmutableVideo.class)
@JsonDeserialize(as = ImmutableVideo.class)
public abstract class Video {

	public abstract String getId();
	public abstract String getMd5Hash();
	public abstract Integer getDuration();
	public abstract String getThumbnailId();
	public abstract Long getSize();
	public abstract LocalDate getUploadDate();
}