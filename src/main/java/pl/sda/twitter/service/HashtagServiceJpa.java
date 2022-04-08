package pl.sda.twitter.service;

import org.springframework.stereotype.Service;
import pl.sda.twitter.model.Hashtag;
import pl.sda.twitter.repository.JpaHashtagRepository;

import java.util.List;

@Service
public class HashtagServiceJpa implements HashtagService {

    public final static int MAX_NUMBER_OF_POPULAR_TWEETS = 2;
    private JpaHashtagRepository jpaHashtagRepository;

    public HashtagServiceJpa(JpaHashtagRepository jpaHashtagRepository) {
        this.jpaHashtagRepository = jpaHashtagRepository;
    }

    @Override
    public List<Hashtag> findAll() {
        return jpaHashtagRepository.findAll();
    }

    @Override
    public List<String> getPopularHashtags() {
        return jpaHashtagRepository.findPopularHashtags().subList(0,MAX_NUMBER_OF_POPULAR_TWEETS);
    }
}
