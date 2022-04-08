package pl.sda.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.twitter.model.Follow;

import java.util.List;

@Repository
public interface JpaFollowersRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByFollower(String follower);
    List<Follow> findAllByUsername(String username);
}
