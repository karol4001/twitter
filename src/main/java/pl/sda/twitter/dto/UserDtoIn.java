package pl.sda.twitter.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoIn {
    private String username;
    private String name;
    private String surname;
    private String password;
    private String email;
}