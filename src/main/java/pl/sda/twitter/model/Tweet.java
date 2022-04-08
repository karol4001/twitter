package pl.sda.twitter.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String content;
    private long authorId;
    private long parentTweetId;
    private int likes;
    private int retweets;
    private int comments;
    private LocalDateTime publishingTime;
}
