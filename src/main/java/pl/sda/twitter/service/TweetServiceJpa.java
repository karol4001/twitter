package pl.sda.twitter.service;

import org.springframework.stereotype.Service;
import pl.sda.twitter.dto.TweetCommentsPage;
import pl.sda.twitter.dto.TweetDtoIn;
import pl.sda.twitter.dto.TweetDtoOut;
import pl.sda.twitter.mapper.TweetMapper;
import pl.sda.twitter.model.*;
import pl.sda.twitter.repository.*;
import pl.sda.twitter.util.HashtagExtractor;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TweetServiceJpa implements TweetService {
    private final JpaTweetRepository jpaTweetRepository;
    private final JpaHashtagRepository jpaHashtagRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaBookmarkRepository jpaBookmarkRepository;
    private final JpaLikesRepository jpaLikesRepository;
    private final JpaFollowersRepository jpaFollowersRepository;

    public TweetServiceJpa(JpaTweetRepository jpaTweetRepository, JpaHashtagRepository jpaHashtagRepository, JpaUserRepository jpaUserRepository, JpaBookmarkRepository jpaBookmarkRepository, JpaLikesRepository jpaLikesRepository, JpaFollowersRepository jpaFollowersRepository) {
        this.jpaTweetRepository = jpaTweetRepository;
        this.jpaHashtagRepository = jpaHashtagRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaBookmarkRepository = jpaBookmarkRepository;
        this.jpaLikesRepository = jpaLikesRepository;
        this.jpaFollowersRepository = jpaFollowersRepository;
    }

    @Override
    @Transactional
    public Optional<Tweet> addNewTweet(User user, TweetDtoIn dto) {
        Tweet savedTweet = saveTweetIntoRepo(dto, -1, user);
        savedTweet.setUsername(user.getUsername());
        return Optional.ofNullable(savedTweet);
    }

    @Transactional
    public Tweet saveTweetIntoRepo(TweetDtoIn dto, long parentTweetId, User user) {
        Tweet tweet = TweetMapper.mapToTweet(dto);
        tweet.setUsername(user.getUsername());
        tweet.setParentTweetId(parentTweetId);
        List<String> strings = HashtagExtractor.extractHashtagStrings(tweet);
        for (String hashtagString : strings) {
            Hashtag hashtag = Hashtag.builder()
                    .tweet(tweet)
                    .label(hashtagString.toUpperCase(Locale.ROOT))
                    .build();
            jpaHashtagRepository.save(hashtag);
        }
        Tweet savedTweet = jpaTweetRepository.save(tweet);
        return savedTweet;
    }

    @Override
    public List<TweetDtoOut> findAllTweets(long userId) {
        List<Tweet> tweets = jpaTweetRepository.findAllByAuthorId(userId);
        return tweets.stream().map(tweet ->
                TweetMapper.mapToTweetDtoOut(tweet)
        ).collect(Collectors.toList());
    }

    @Override
    public List<TweetDtoOut> findAllTweetsByUsername(String username) {
        List<Tweet> tweets = jpaTweetRepository.findAllByUsername(username);
        return tweets.stream().map(TweetMapper::mapToTweetDtoOut).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<TweetCommentsPage> getTweetComments(long parentTweetId) {
        Tweet tweet = jpaTweetRepository.getById(parentTweetId);
        List<Tweet> comments = jpaTweetRepository.findAllByParentTweetId(parentTweetId);
        if (comments.isEmpty()) {
            return Optional.of(new TweetCommentsPage(TweetMapper.mapToTweetDtoOut(tweet), Collections.emptyList()));
        }
        TweetDtoOut tweetDtoOut = TweetMapper.mapToTweetDtoOut(tweet);
        TweetCommentsPage tweetCommentsPage = new TweetCommentsPage(tweetDtoOut, comments);
        return Optional.ofNullable(tweetCommentsPage);
    }

    @Override
    @Transactional
    public Optional<Tweet> addComment(long parentTweetId, TweetDtoIn tweetDtoIn) {
        Tweet parentTweet = jpaTweetRepository.getById(parentTweetId);

        Tweet commentTweet = saveTweetIntoRepo(tweetDtoIn, parentTweetId, jpaUserRepository.findUserByUsername(parentTweet.getUsername()).get());

        return Optional.ofNullable(commentTweet);
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    @Transactional
    public List<TweetDtoOut> findAllTweetsContainingWords(String word) {
        List<Tweet> tweetsByWord = jpaTweetRepository.findAllByContentIsContaining(word);
        return tweetsByWord.stream().map(tweet ->
                TweetMapper.mapToTweetDtoOut(tweet)
        ).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public String addTweetLike(String username, long id) {
        final Optional<Tweet> opTweet = jpaTweetRepository.findById(id);
        Tweet tweet = opTweet.get();
        List<Like_> likes = jpaLikesRepository.findAllByUsername(username);
        for(Like_ l : likes){
            if(l.getTweetId() == id){
                return "Odmowa: like do tweeta o id " + id + " już był dodany przez użytkowanika " + username;
            }
        }
        if (opTweet.isPresent()) {
            int likesCounter = tweet.getLikes();
            likesCounter++;
            tweet.setLikes(likesCounter);
            jpaTweetRepository.save(tweet);
            Like_ newLike = Like_.builder()
                    .tweetId(id)
                    .username(username)
                    .build();
            jpaLikesRepository.save(newLike);
        }
        return "Like uzutkownika "+username+" do tweeta o id  "+id+" zostal dodany";
    }

    @Override
    public void deleteTweetById(long id) {
        jpaTweetRepository.deleteById(id);
    }

    @Override
    public Optional<Tweet> addBookmark(User user, TweetDtoIn tweet) {
        Tweet bookmark = TweetMapper.mapToTweet(tweet);
        /**
         * wykorzystujemy parentTweetId żeby przyporządkować bookmark do usera który go dodał
         * W bookmarkach parentTweetId to będzie właściciel
         * **/
        bookmark.setParentTweetId(jpaUserRepository.findUserByUsername(user.getUsername()).get().getId());

        Tweet savedBookmark = jpaBookmarkRepository.save(bookmark);



        return Optional.ofNullable(savedBookmark);
    }

    @Override
    public List<Tweet> findAllBookmarks() {
        List<Tweet> bookmarks = jpaBookmarkRepository.findAll();
        System.out.println(bookmarks.size());
        return bookmarks;
    }

    @Override
    public List<TweetDtoOut> findAllTweetsFromFollowedUsers(List<User> users) {
        List<TweetDtoOut> tweetsFromFollowedUsers = new ArrayList<>();
        for(User u : users) {
            List<Tweet> tweets = jpaTweetRepository.findAllByUsername(u.getUsername());
            for(Tweet t : tweets) {
                tweetsFromFollowedUsers.add(TweetMapper.mapToTweetDtoOut(t));
            }
        }
        return tweetsFromFollowedUsers;
    }




}