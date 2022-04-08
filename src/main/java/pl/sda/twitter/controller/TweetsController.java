package pl.sda.twitter.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.sda.twitter.dto.TweetCommentsPage;
import pl.sda.twitter.dto.TweetDtoIn;
import pl.sda.twitter.dto.TweetDtoOut;
//import pl.sda.twitter.model.Notification;
import pl.sda.twitter.model.Notification_;
import pl.sda.twitter.model.Tweet;
import pl.sda.twitter.model.User;
import pl.sda.twitter.repository.JpaBookmarkRepository;
//import pl.sda.twitter.repository.JpaNotificationsRepository;
import pl.sda.twitter.repository.JpaNotificationsRepository;
import pl.sda.twitter.repository.JpaTweetRepository;
import pl.sda.twitter.repository.JpaUserRepository;
import pl.sda.twitter.service.TweetService;
import pl.sda.twitter.service.UserService;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")

public class TweetsController {

    private final TweetService tweetService;
    private final JpaUserRepository userRepository;
    private final JpaBookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final JpaNotificationsRepository jpaNotificationsRepository;
    private final JpaTweetRepository jpaTweetRepository;

    public TweetsController(TweetService tweetService, JpaUserRepository userRepository, JpaBookmarkRepository bookmarkRepository, UserService userService, JpaNotificationsRepository jpaNotificationsRepository, JpaTweetRepository jpaTweetRepository) {
        this.tweetService = tweetService;
        this.userRepository = userRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.userService = userService;
        this.jpaNotificationsRepository = jpaNotificationsRepository;


        this.jpaTweetRepository = jpaTweetRepository;
    }

    @GetMapping("/{id}")
    public List<TweetDtoOut> findTweetsByUser(@PathVariable long id) {
        return tweetService.findAllTweets(id);
    }

    @GetMapping("/user/{username}")
    public List<TweetDtoOut> findTweetsByUsername(@PathVariable String username) {
        List<TweetDtoOut> ownedTweets = tweetService.findAllTweetsByUsername(username);
        List<User> followedUsers = userService.findAllFollowedByUser(username);
        ownedTweets.addAll(tweetService.findAllTweetsFromFollowedUsers(followedUsers));
        return ownedTweets;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    ResponseEntity<Tweet> add(@AuthenticationPrincipal User user, @PathVariable long id, @RequestBody TweetDtoIn dto) {
        Optional<User> userById = userRepository.findById(id);
        System.out.println(user.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.addNewTweet(userById.get(), dto).get());
    }

    @GetMapping("/comments/{user}/{id}")
    public ResponseEntity<TweetCommentsPage> getComments(@PathVariable(name = "id") long parentTweetId) {

//        @RequestHeader HttpHeaders headers
//        System.out.println("User has authorities: " + headers);

        return ResponseEntity.of(tweetService.getTweetComments(parentTweetId));
    }

    @PostMapping("/comments/{id}" )
    public ResponseEntity<Tweet> addComment(@AuthenticationPrincipal User user, @PathVariable(name = "id") long parentTweetId, @RequestBody TweetDtoIn dto) {
        Notification_ notification = new Notification_();
        Optional<Tweet> tweet = jpaTweetRepository.findById(parentTweetId);

/**        dorobić zwiększanie ilości komentarzy **/
//        tweet.get().setComments(tweet.get().getComments()+1);
        if (tweet.isPresent()) {
            notification.builder()
                    .username(user.getUsername())
                    .notification("Użytkownik "+ user.getUsername() +" dodał komentarz do tweeta użytkownika "+ tweet.get().getUsername())
                    .build();
            jpaNotificationsRepository.save(notification);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.addComment(parentTweetId, dto).get());
    }

    @GetMapping("/search/tweet/{word}")
    public List<TweetDtoOut> findTweetsByWord(@PathVariable String word) {
        return tweetService.findAllTweetsContainingWords(word);
    }

    @PostMapping("/tweet/like/{user}/{id}")
    public ResponseEntity<String> addTweetLike(@PathVariable(name = "user") String username, @PathVariable(name = "id") long id) {
        String likeAddProcess = tweetService.addTweetLike(username, id);
        System.out.println(likeAddProcess);
        return ResponseEntity.status(HttpStatus.CREATED).body(likeAddProcess);
    }

    @GetMapping("image/{name}")
    public ResponseEntity showImage(@PathVariable String name) throws IOException {
        File file = new File("uploads/" + name);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(URLConnection.guessContentTypeFromName(name)))
                .body(Files.readAllBytes(file.toPath()));
    }

    @PostMapping("/upload")
    @ResponseBody
    public String handleFile(@RequestPart(name = "fileupload") MultipartFile file) { // jako parametr metody przyjmujemy MultipartFile o nazwie fileupload (nazwa ta musi być taka sama jak nazwa pola w formularzu).
        File uploadDirectory = new File("uploads");
        uploadDirectory.mkdirs(); // upewniam się, że katalog, do którego chcę zapisać plik, istnieje, a jeśli nie, to go tworzę
        File oFile = new File("uploads/" + file.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(oFile);
             InputStream inputStream = file.getInputStream()) {
            IOUtils.copy(inputStream, os); // pobranie strumienia wejściowego z przesłanego pliku i przekopiowanie zawartość do stworzonego strumienia wyjsciowego
        } catch (IOException e) {
            e.printStackTrace();
            return "There was an error uploading the file: " + e.getMessage();
        }
        return "File uploaded!";
    }

    @DeleteMapping("/tweet/{id}")
    public void delete(@PathVariable long id){
        tweetService.deleteTweetById(id);
    }

    @PostMapping("/bookmark/{user}/{id}")
    ResponseEntity<Tweet> addBookmark(@PathVariable(name = "username") String username, @PathVariable(name = "id") long id, @RequestBody TweetDtoIn dto) {
        Optional<User> user = userRepository.findUserByUsername(username);
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.addBookmark(user.get(), dto).get());
    }

    @GetMapping("/bookmarklist")
    public List<Tweet> findBookmarks() {
        return tweetService.findAllBookmarks();
    }
}
