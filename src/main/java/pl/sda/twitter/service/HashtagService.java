package pl.sda.twitter.service;

import pl.sda.twitter.model.Hashtag;

import java.util.List;

public interface HashtagService {
    List<Hashtag> findAll();
    List<String> getPopularHashtags();
}
