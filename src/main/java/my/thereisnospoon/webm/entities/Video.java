package my.thereisnospoon.webm.entities;

import org.immutables.value.Value;

import java.time.LocalDate;

@Value.Immutable
public abstract class Video {

	public abstract String getId();
	public abstract String getMd5Hash();
	public abstract Integer getDuration();
	public abstract String getThumbnailId();
	public abstract Long getSize();
	public abstract LocalDate getUploadDate();
}