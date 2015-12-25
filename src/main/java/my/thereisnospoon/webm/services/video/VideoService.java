package my.thereisnospoon.webm.services.video;

import my.thereisnospoon.webm.vo.Video;

public interface VideoService {

	Video processAndSaveVideo(byte[] videoData) throws Exception;

	void likeVideo(String videoId, String username);

	void removeLikeFromVideo(String videoId, String username);

	void incrementViewsCounter(String videoId);
}
