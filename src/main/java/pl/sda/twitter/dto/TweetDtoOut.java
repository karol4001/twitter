package pl.sda.twitter.dto;

import lombok.*;
import pl.sda.twitter.model.Hashtag;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TweetDtoOut {

    private long id;
    private String content;
    private int likes;
    private int retweets;
    private int comments;
    private String username;
    private String publishingTime;

}
