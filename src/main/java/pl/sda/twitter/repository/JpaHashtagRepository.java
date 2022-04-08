package pl.sda.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.sda.twitter.model.Hashtag;

import java.util.List;

@Repository
public interface JpaHashtagRepository extends JpaRepository<Hashtag, Long> {
    @Query("SELECT h.label FROM Hashtag h GROUP BY h.label ORDER BY COUNT (h) DESC")
    List<String> findPopularHashtags();
}
