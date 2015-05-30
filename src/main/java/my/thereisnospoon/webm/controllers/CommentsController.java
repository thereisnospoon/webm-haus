package my.thereisnospoon.webm.controllers;

import my.thereisnospoon.webm.entities.Comment;
import my.thereisnospoon.webm.entities.User;
import my.thereisnospoon.webm.entities.repos.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping(value = "/comments")
public class CommentsController {

	private static final Logger log = LoggerFactory.getLogger(CommentsController.class);

	@Autowired
	private CommentRepository commentRepository;

	@RequestMapping(value = "/{webmId}", method = RequestMethod.GET)
	public Collection<Comment> getCommentsForWebM(@PathVariable String webmId, Pageable pageable) {

		return commentRepository.getCommentsByWebmIdOrderByPostedWhenDesc(webmId, pageable).getContent();
	}

	@Secured(User.ROLE_USER)
	@RequestMapping(method = RequestMethod.POST)
	public Comment postComment(@RequestBody Comment comment, @AuthenticationPrincipal User user) {

		comment.setPostedWhen(new Date());
		comment.setAuthor(user.getUsername());

		log.debug("Posting comment: {}", comment);

		comment = commentRepository.insert(comment);
		return comment;
	}
}
