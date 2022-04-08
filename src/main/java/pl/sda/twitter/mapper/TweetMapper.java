package pl.sda.twitter.mapper;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.sda.twitter.dto.TweetDtoIn;
import pl.sda.twitter.dto.TweetDtoOut;
import pl.sda.twitter.model.Tweet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TweetMapper {
    public final static int NOT_A_COMMENT_TWEET_ID = -1;

    static public TweetDtoOut mapToTweetDtoOut(Tweet tweet) {
        return TweetDtoOut.builder()
                .id(tweet.getId())
                .content(tweet.getContent())
                .comments(tweet.getComments())
                .likes(tweet.getLikes())
                .comments(tweet.getComments())
                .username(tweet.getUsername())
                .publishingTime(timeMapper(tweet.getPublishingTime()))
                .build();
    }

    static public Tweet mapToTweet(TweetDtoIn dtoIn) {
        return Tweet.builder()
                .content(dtoIn.getContent())
                .likes(0)
                .comments(0)
                .retweets(0)
                .parentTweetId(NOT_A_COMMENT_TWEET_ID)
                .authorId(dtoIn.getAuthor())
                .build();
    }

    public static String timeMapper(LocalDateTime localDateTime){
        if(localDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return localDateTime.format(formatter); // "1986-04-08 12:30"
        } else {
            return "";
        }

    }
}
