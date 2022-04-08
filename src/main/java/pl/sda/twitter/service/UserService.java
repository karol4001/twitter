package pl.sda.twitter.service;

import pl.sda.twitter.dto.UserDtoOut;
import pl.sda.twitter.dto.UserLoginForm;
import pl.sda.twitter.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService{
    List<UserDtoOut> findAllUsersContainingWords(String word);
    Optional<UserDtoOut> UserLogin(UserLoginForm login);
    String followUser(String username, String follower);
    String unfollowUser(String username, String follower);
    List<User> findAllFollowers(String username);
    List<User> findAllFollowedByUser(String follower);


}
