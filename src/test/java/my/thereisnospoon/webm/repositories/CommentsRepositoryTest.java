package my.thereisnospoon.webm.repositories;

import my.thereisnospoon.webm.AbstractIntegrationTest;
import my.thereisnospoon.webm.vo.Comment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentsRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	private CommentsRepository commentsRepository;

	private Comment comment;
	private Comment childComment;

	@Before
	public void setUp() {

		comment = createComment();
		comment = commentsRepository.save(comment);

		childComment = createComment();
		childComment = commentsRepository.save(childComment);
	}

	private Comment createComment() {

		return Comment.builder()
				.text("text")
				.timestamp(new Date())
				.username(TEST_USERNAME)
				.videoId(TEST_VIDEO_ID)
				.build();
	}

	@After
	public void cleanUp() {

		if (commentsRepository.exists(childComment.getId())) {
			commentsRepository.delete(childComment);
		}

		if (commentsRepository.exists(comment.getId())) {
			commentsRepository.delete(comment);
		}
	}

	@Test
	public void shouldDeleteChildComment() {

		commentsRepository.delete(childComment.getId());
		assertTrue(commentsRepository.exists(comment.getId()));
		assertFalse(commentsRepository.exists(childComment.getId()));
	}
}