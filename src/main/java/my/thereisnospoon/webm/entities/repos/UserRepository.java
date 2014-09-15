package my.thereisnospoon.webm.entities.repos;

import my.thereisnospoon.webm.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
}
