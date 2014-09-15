package my.thereisnospoon.webm.entities.repos;

import my.thereisnospoon.webm.entities.WebMPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebMRepository extends MongoRepository<WebMPost,String> {


}
