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
				.username(TEST_USERNAME)
				.key(Comment.Key.builder()
						.ts(new Date())
						.videoId(TEST_VIDEO_ID)
						.build())
				.build();
	}

	@After
	public void cleanUp() {

		if (commentsRepository.exists(childComment.getKey())) {
			commentsRepository.delete(childComment);
		}

		if (commentsRepository.exists(comment.getKey())) {
			commentsRepository.delete(comment);
		}
	}

	@Test
	public void shouldDeleteChildComment() {

		commentsRepository.delete(childComment);
		assertFalse(commentsRepository.exists(childComment.getKey()));
		assertTrue(commentsRepository.exists(comment.getKey()));
	}
}