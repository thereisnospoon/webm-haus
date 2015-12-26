package my.thereisnospoon.webm.repositories;

import my.thereisnospoon.webm.vo.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends CrudRepository<Comment, Long> {
}
