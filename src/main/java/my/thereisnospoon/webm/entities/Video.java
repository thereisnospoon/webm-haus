package my.thereisnospoon.webm.entities;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Video {

	public abstract String id();
	public abstract String md5Hash();
	public abstract Integer duration();
	public abstract String thumbnailId();
	public abstract Long size();
}