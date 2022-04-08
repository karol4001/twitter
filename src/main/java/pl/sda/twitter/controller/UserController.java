package pl.sda.twitter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.sda.twitter.dto.UserDtoIn;
import pl.sda.twitter.dto.UserDtoOut;
import pl.sda.twitter.dto.UserLoginForm;
import pl.sda.twitter.model.User;
import pl.sda.twitter.repository.JpaUserRepository;
import pl.sda.twitter.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin (origins = "*")
public class UserController {
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final JpaUserRepository jpaUserRepository;


    public UserController(PasswordEncoder passwordEncoder, UserService userService, JpaUserRepository jpaUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jpaUserRepository = jpaUserRepository;
    }

    @GetMapping("/search/user/{word}")
    public List<UserDtoOut> findUsersByWord(@PathVariable String word) {
        return userService.findAllUsersContainingWords(word);
    }

    @GetMapping("/user/login/{data}")
    public ResponseEntity<UserDtoOut> loginUser(@PathVariable String data){
        String[] tab = data.split("-");
        UserLoginForm login = new UserLoginForm(tab[0], tab[1]);
        Optional<UserDtoOut> userDtoOut = userService.UserLogin(login);
      return  ResponseEntity.of(userDtoOut);
    }

    @RequestMapping(value = "/user/{follower}/follow/{username}", method = RequestMethod.POST)
    public ResponseEntity<String> follow(@PathVariable(name = "username") String username, @PathVariable(name = "follower") String follower) {
        String response = userService.followUser(username, follower);
        System.out.println(userService.findAllFollowedByUser(username));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public ResponseEntity<String> addUser(@RequestBody UserDtoIn userDtoIn) {
        User newUser = User.builder()
                .name(userDtoIn.getName())
                .surname(userDtoIn.getSurname())
                .username(userDtoIn.getUsername())
                .password(passwordEncoder.encode(userDtoIn.getPassword()))
                .build();
        User savedUser = jpaUserRepository.save(newUser);
        String response  = "Utworzono usera o id: " + savedUser.getId() + " oraz o nicku: " + newUser.getUsername();
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
