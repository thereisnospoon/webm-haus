package my.thereisnospoon.webm.services.video.impl;

import com.mongodb.gridfs.GridFSFile;
import my.thereisnospoon.webm.entities.ImmutableVideo;
import my.thereisnospoon.webm.entities.Video;
import my.thereisnospoon.webm.services.gridfs.ContentType;
import my.thereisnospoon.webm.services.gridfs.GridFsService;
import my.thereisnospoon.webm.services.video.VideoService;
import my.thereisnospoon.webm.services.video.exception.VideoAlreadyExistsException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VideoServiceImpl implements VideoService {

	private static final Logger log = LoggerFactory.getLogger(VideoServiceImpl.class);

	private static final Pattern DURATION_PATTERN = Pattern.compile("Duration: (\\d{2}):(\\d{2}):(\\d{2})");

	@Value("${ffmpeg}")
	private String ffmpegLocation;

	@Value("${ffprobe}")
	private String ffprobeLocation;

	@Value("${thumbnail_location}")
	private String thumbnailLocation;

	@Value("${temp_files_location}")
	private String tempFolderLocation;

	@Autowired
	private GridFsService gridFsService;

	@Override
	public Video processAndSaveVideo(byte[] videoData) throws Exception {

		String videoHash = calculateDataHash(videoData);
		String tempVideoFilePath = saveFileInTempFolder(videoData, videoHash);
		int videoDuration = getVideoDuration(tempVideoFilePath);
		byte[] thumbnailData = getThumbnail(tempVideoFilePath, videoHash);

		deleteFileFromTempFolder(videoHash);
		ensureVideoUniqueness(videoHash);

		GridFSFile videoFileInDB = gridFsService.storeData(videoData, ContentType.VIDEO);
		String videoId = videoFileInDB.getId().toString();
		Long videoSize = videoFileInDB.getLength();

		GridFSFile thumbnailFileInDB = gridFsService.storeData(thumbnailData, ContentType.IMAGE);
		String thumbnailId = thumbnailFileInDB.getId().toString();

		return ImmutableVideo.builder()
				.duration(videoDuration)
				.md5Hash(videoHash)
				.thumbnailId(thumbnailId)
				.id(videoId)
				.size(videoSize)
				.build();
	}

	private String calculateDataHash(byte[] data) {
		return DigestUtils.md5Hex(data);
	}

	private void ensureVideoUniqueness(String videoHash) {

		if (gridFsService.isDataUnique(videoHash, ContentType.VIDEO)) {
			throw new VideoAlreadyExistsException();
		}
	}

	/**
	 * Returns video duration in seconds
	 * @param videoFilePath absolute path to given video
	 * @return  duration in seconds
	 * @throws Exception
	 */
	private int getVideoDuration(String videoFilePath) throws Exception {

		log.debug("Trying to get duration of {}", videoFilePath);

		ProcessBuilder processBuilder = new ProcessBuilder(ffprobeLocation, "-i", videoFilePath);
		Optional<Integer> extractedDuration = new BufferedReader(new InputStreamReader(processBuilder.start()
				.getErrorStream())).lines()
				.peek(line -> log.debug("{}", line))
				.filter(line -> line.contains("Duration:"))
				.map(lineWithDuration -> {

                    Matcher matcher = DURATION_PATTERN.matcher(lineWithDuration);
                    boolean found = matcher.find();
                    if (!found) {
                        throw new IllegalArgumentException("Couldn't extract duration from given video");
                    }
                    String hours = matcher.group(1);
                    String minutes = matcher.group(2);
                    String seconds = matcher.group(3);

                    return Integer.parseInt(hours) * 60 * 60
                            + Integer.parseInt(minutes) * 60
                            + Integer.parseInt(seconds);
                }).findAny();

		return extractedDuration.get();
	}

	/**
	 * Generates thumbnail for video using ffmpeg.
	 * The following command is used:
	 *      ffmpeg -i <path to video> -s 320x180 -frames:v 1 <path to output image>
	 *
	 * @param videoFilePath path to video file
	 * @param videoHash hash value for given video which is used to uniquely name generated thumbnail file
	 * @return  generated thumbnail as byte[] array
	 * @throws Exception
	 */
	private byte[] getThumbnail(String videoFilePath, String videoHash) throws Exception {

		log.debug("Trying to get thumbnail for: {}", videoFilePath);

		String outputImageAbsolutePath = thumbnailLocation + File.separator + videoHash + ".png";
		ProcessBuilder processBuilder = new ProcessBuilder(ffmpegLocation,
				"-i", videoFilePath, "-s", "320x180", "-frames:v", "1", outputImageAbsolutePath);

		Optional<String> output = new BufferedReader(new InputStreamReader(processBuilder.start().getErrorStream()))
                .lines()
                .reduce((line1, line2) -> line1 + "\n" + line2);

        log.debug("ffmpeg output: {}", output);

		File file = new File(outputImageAbsolutePath);
		if (!file.exists()) {
			throw new IllegalStateException("Something went wrong during thumbnail creation");
		} else {
            byte[] thumbnail = Files.readAllBytes(Paths.get(outputImageAbsolutePath));
            if (!file.delete()) {
                log.debug("Couldn't delete generated thumbnail: {}", outputImageAbsolutePath);
            }
            return thumbnail;
        }
	}

	/**
	 * Creates file in temporary folder
	 * @param fileData  file binary data
	 * @param hash  file's md5 hash that is being used as file name
	 * @return  absolute path to created file
	 * @throws IOException
	 */
	private String saveFileInTempFolder(byte[] fileData, String hash) throws IOException {

		File tempFile = new File(tempFolderLocation, hash);
		if (tempFile.exists()) {

			log.debug("File {}/{} already exists and will be removed", tempFolderLocation, hash);

			boolean isDeleted = tempFile.delete();

			log.debug("Deletion result: {}", isDeleted);
		}
		try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile))) {
			outputStream.write(fileData);
		}
		return tempFile.getAbsolutePath();
	}

	/**
	 * Deletes file from temp folder
	 * @param fileHash  file's md5 hash that is being used as file name
	 * @return  true if deletion was successful
	 * @throws IOException
	 */
	private boolean deleteFileFromTempFolder(String fileHash) throws IOException {

		File tempFile = new File(tempFolderLocation, fileHash);
		if (tempFile.exists()) {
			return tempFile.delete();
		} else {

			log.debug("File {} doesn't exist", tempFile.getAbsolutePath());

			return false;
		}
	}
}
