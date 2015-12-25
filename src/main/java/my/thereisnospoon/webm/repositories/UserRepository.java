package my.thereisnospoon.webm.repositories;

import my.thereisnospoon.webm.vo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {


}
