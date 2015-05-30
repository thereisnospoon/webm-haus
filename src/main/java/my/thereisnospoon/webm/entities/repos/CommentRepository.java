package my.thereisnospoon.webm.entities.repos;

import my.thereisnospoon.webm.entities.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {

	Slice<Comment> getCommentsByWebmIdOrderByPostedWhenDesc(String webmId, Pageable pageable);
}
