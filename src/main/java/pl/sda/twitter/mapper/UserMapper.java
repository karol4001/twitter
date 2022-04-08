package pl.sda.twitter.mapper;

import pl.sda.twitter.dto.UserDtoIn;
import pl.sda.twitter.dto.UserDtoOut;
import pl.sda.twitter.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserMapper {

    static public UserDtoOut mapToUserDtoOut(User user) {
        return UserDtoOut.builder()
                .username(user.getUsername())
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
    }
    static public User mapToUser(UserDtoIn dtoIn) {
        return User.builder()
                .name(dtoIn.getName())
                .username(dtoIn.getUsername())
                .build();
    }


    private static String timeMapper(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter); // "1986-04-08 12:30"
}
}
