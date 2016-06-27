package my.thereisnospoon.webm.services.video;

import my.thereisnospoon.webm.vo.Video;

public interface VideoService {

	Video processAndSaveVideo(byte[] videoData) throws Exception;

	void incrementViewsCounter(String videoId);
}
