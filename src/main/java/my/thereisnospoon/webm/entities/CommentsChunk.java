package my.thereisnospoon.webm.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "comments")
public class CommentsChunk {

	private List<Comment> comments;
	private String nextChunkId;
}
