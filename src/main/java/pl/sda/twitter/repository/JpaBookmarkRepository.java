package pl.sda.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.twitter.model.Tweet;

import java.util.List;

@Repository
public interface JpaBookmarkRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findAll();
}
