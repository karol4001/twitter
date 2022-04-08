package pl.sda.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.twitter.model.Like_;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaLikesRepository extends JpaRepository<Like_, Long> {

    Optional<Like_> findAllByUsernameAndTweetId(long id, String username);
    List<Like_> findAllByUsername(String username);
}
