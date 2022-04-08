package pl.sda.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sda.twitter.model.Notification_;

import java.util.List;

@Repository
public interface JpaNotificationsRepository extends JpaRepository<Notification_, Long> {
    List<Notification_> findAllByUsername(String username);
}
