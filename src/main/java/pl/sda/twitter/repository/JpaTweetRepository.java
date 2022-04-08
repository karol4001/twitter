package pl.sda.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.twitter.model.Tweet;

import java.util.List;

@Repository
public interface JpaTweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findAllByAuthorId(long userId);
    List<Tweet> findAllByUsername(String username);
    List<Tweet> findAllByParentTweetId(long parentTweetId);
    List<Tweet> findAllByContentIsContaining(String word);
//    Tweet findById(long id);
}

