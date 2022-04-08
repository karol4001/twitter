package pl.sda.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sda.twitter.model.Tweet;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TweetCommentsPage {
    private TweetDtoOut originalTweet;
    private List<Tweet> tweets;

}
