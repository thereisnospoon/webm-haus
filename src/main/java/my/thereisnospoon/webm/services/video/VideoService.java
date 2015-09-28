package my.thereisnospoon.webm.services.video;

import my.thereisnospoon.webm.entities.Video;

public interface VideoService {

	Video processAndSaveVideo(byte[] videoData) throws Exception;
}
