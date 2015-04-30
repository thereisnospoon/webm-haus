package my.thereisnospoon.webm.services;

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

/**
 * Gets video-file meta information
 */
@Service
public class FFMPEGService {

	private static final Logger log = LoggerFactory.getLogger(FFMPEGService.class);

	private static final Pattern DURATION_PATTERN = Pattern.compile("Duration: (\\d{2}):(\\d{2}):(\\d{2})");

	private String ffmpegLocation;
	private String ffprobeLocation;
	private String thumbnailLocation;
	private String tempFolderLocation;

	@Autowired
	public FFMPEGService(@Value("${ffmpeg}") String ffmpegLocation,
	                     @Value("$ffprobe") String ffprobeLocation,
	                     @Value("${thumbnail_location}") String thumbnailLocation,
	                     @Value("$temp_files_location") String tempFolderLocation) {

		this.tempFolderLocation = tempFolderLocation;
		this.ffmpegLocation = ffmpegLocation;
		this.ffprobeLocation = ffprobeLocation;
		this.thumbnailLocation = thumbnailLocation;
	}

	/**
	 * Returns video duration in seconds
	 * @param videoFilePath absolute path to given video
	 * @return  duration in seconds
	 * @throws Exception
	 */
	public int getVideoDuration(String videoFilePath) throws Exception {

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
	public byte[] getThumbnail(String videoFilePath, String videoHash) throws Exception {

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
	 * @return  absolute path to created path
	 * @throws IOException
	 */
	public String saveFileInTempFolder(byte[] fileData, String hash) throws IOException {

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
	public boolean deleteFileFromTempFolder(String fileHash) throws IOException {

		File tempFile = new File(tempFolderLocation, fileHash);
		if (tempFile.exists()) {
			return tempFile.delete();
		} else {

			log.debug("File {} doesn't exist", tempFile.getAbsolutePath());

			return false;
		}
	}
}
