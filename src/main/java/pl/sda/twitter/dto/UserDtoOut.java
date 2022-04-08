package pl.sda.twitter.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoOut {
    private long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private LocalDateTime creationDateTime;
    private int numberOfTweets;
    private int numberOfFollowers;
    private int numberOfPeopleWatched;
}