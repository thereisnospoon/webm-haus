package my.thereisnospoon.webm.repositories;

import my.thereisnospoon.webm.vo.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends CrudRepository<Video, String> {

}
