package pl.sda.twitter.service;

import org.springframework.stereotype.Service;
import pl.sda.twitter.dto.UserDtoOut;
import pl.sda.twitter.dto.UserLoginForm;
import pl.sda.twitter.mapper.UserMapper;
import pl.sda.twitter.model.Follow;
import pl.sda.twitter.model.User;
import pl.sda.twitter.repository.JpaFollowersRepository;
import pl.sda.twitter.repository.JpaUserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceJpa implements UserService {
    private final JpaUserRepository jpaUserRepository;
    private final JpaFollowersRepository jpaFollowersRepository;

    public UserServiceJpa(JpaUserRepository jpaUserRepository, JpaFollowersRepository jpaFollowersRepository) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaFollowersRepository = jpaFollowersRepository;
    }

    @Override
    @Transactional
    public List<UserDtoOut> findAllUsersContainingWords(String word) {
        List<User> usersByWord = jpaUserRepository.findUsersByUsernameContaining(word);
        return usersByWord.stream().map(user ->
                UserMapper.mapToUserDtoOut(user)
        ).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDtoOut> UserLogin(UserLoginForm login) {
        Optional<User> userByUsername = jpaUserRepository.findUserByUsername(login.getUsername());
        if (userByUsername.isPresent()){
            User user = userByUsername.get();
            return Optional.of(UserMapper.mapToUserDtoOut(user));
        }
        return Optional.empty();
    }


    @Override
    public String followUser(String username, String follower) {
        Follow newFollower = Follow.builder()
                .follower(follower)
                .username(username)
                .build();
        jpaFollowersRepository.save(newFollower);
        return "Użytkownik "+follower+" obserwuje użytkownika "+username;
    }

    @Override
    public String unfollowUser(String username, String follower) {
        List<Follow> followers = jpaFollowersRepository.findAllByUsername(username);
        for(Follow f : followers){
            if(f.getUsername().equals(follower)){
                jpaFollowersRepository.delete(f);
                return "Użytkownik "+follower+" przestał obserwować użytkownika "+username;
            }
        }
        return "Nieaktualne?, użytkownik "+follower+" nie obserwował użytkownika "+username;
    }

    @Override
    public List<User> findAllFollowers(String username) {
        List<Follow> allFollowers = jpaFollowersRepository.findAllByUsername(username);
        List<User> followers = new ArrayList<>();
        for(Follow f : allFollowers){
            followers.add(jpaUserRepository.findUserByUsername(f.getFollower()).get());
        }
        return followers;
    }

    @Override
    public List<User> findAllFollowedByUser(String follower) {
        List<Follow> allFollowed = jpaFollowersRepository.findAllByFollower(follower);
        List<User> followed = new ArrayList<>();
        for(Follow f : allFollowed){
            followed.add(jpaUserRepository.findUserByUsername(f.getUsername()).get());
        }
        return followed;
    }

}
