package pl.sda.twitter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sda.twitter.dto.TweetDtoIn;
import pl.sda.twitter.model.Tweet;
import pl.sda.twitter.model.User;
import pl.sda.twitter.repository.JpaUserRepository;
import pl.sda.twitter.service.HashtagService;
import pl.sda.twitter.service.TweetService;
import pl.sda.twitter.util.HashtagExtractor;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootApplication
public class TwitterApplication implements CommandLineRunner {
    private final TweetService tweetService;
    private final JpaUserRepository userRepository;
    private final HashtagService hashtagService;

    public TwitterApplication(TweetService tweetService, JpaUserRepository userRepository, HashtagService hashtagService) {
        this.tweetService = tweetService;
        this.userRepository = userRepository;
        this.hashtagService = hashtagService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterApplication.class, args);
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        User user1 = User.builder()
                .name("Jan")
                .surname("Konieczny")
                .username("jan")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
//                .password("1234")
                .build();

        User savedUser1 = userRepository.save(user1);
        System.out.println("Utworzono usera o id: " + savedUser1.getId() + " oraz o nicku: " + user1.getUsername());

        User user2 = User.builder()
                .name("Damian")
                .id(2)
                .surname("Kowalski")
                .username("damian")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser2 = userRepository.save(user2);
        System.out.println("Utworzono usera o id: " + savedUser2.getId() + " oraz o nicku: " + user2.getUsername());

        TweetDtoIn tweetDtoIn1 = TweetDtoIn.builder()
                .content("Exciting time in commercial spaceflight. Polaris #Program will undertake a series of tech demo missions culminating in first flight of Starship.")
                .author(savedUser1.getId())
                .build();

        Optional<Tweet> tweet1 = tweetService.addNewTweet(user1, tweetDtoIn1);
        System.out.println("Utworzono tweeta o id: " + tweet1.get().getId());

        TweetDtoIn tweetDtoIn2 = TweetDtoIn.builder()
                .content("The duty of a leader is to serve their people, not for the people to serve them")
                .build();

        Optional<Tweet> tweet2 = tweetService.addNewTweet(user1, tweetDtoIn2);
        System.out.println("Utworzono tweeta o id: " + tweet2.get().getId());

        TweetDtoIn tweetDtoIn3 = TweetDtoIn.builder()
                .content("The National Academy of Engineering is excited to #announce the election of 111 new members and 22 international members. The newly elected members will be formally inducted at our annual meeting on Oct 2.")
                .build();

        Optional<Tweet> tweet3 = tweetService.addNewTweet(user1, tweetDtoIn3);
        System.out.println("Utworzono tweeta o id: " + tweet3.get().getId());

        TweetDtoIn tweetDtoIn4 = TweetDtoIn.builder()
                .content("POV: Jungkook stopped in the middle of walking just to see u standing there, ur 2nd last face emoji is ur reaction !")
                .build();

        Optional<Tweet> tweet4 = tweetService.addNewTweet(user1, tweetDtoIn4);
        System.out.println("Utworzono tweeta o id: " + tweet4.get().getId());

        TweetDtoIn tweetDtoIn5 = TweetDtoIn.builder()
                .content("Przyjemność ponad wszystko. Nowy, hybrydowy Peugeot 308 już w salonach. Sprawdź wyjątkowe oferty leasingowe. Tak wyjątkowe, jak Twój biznes.")
                .build();

        Optional<Tweet> tweet5 = tweetService.addNewTweet(user2, tweetDtoIn5);
        System.out.println("Utworzono tweeta o id: " + tweet5.get().getId());

        TweetDtoIn tweetDtoIn6 = TweetDtoIn.builder()
                .content("U.S. Birthrate Fell By 4% In 2020, Hitting Another #Record LowFor the sixth year in a row, the number of U.S. births fell in 2020, reaching the lowest level since 1979. The fertility rate remains below ")
                .build();

        Optional<Tweet> tweet6 = tweetService.addNewTweet(user1, tweetDtoIn6);
        System.out.println("Utworzono tweeta o id: " + tweet6.get().getId());

        TweetDtoIn tweetDtoIn7 = TweetDtoIn.builder()
                .content("CEO, @dsn_ #software, @Cytek_Security, and @TechEvolutions | Revolutionizing Digital Healthcare | Transforming Cybersecurity #jobs")
                .build();

        Optional<Tweet> tweet7 = tweetService.addNewTweet(user2, tweetDtoIn7);
        System.out.println("Utworzono tweeta o id: " + tweet7.get().getId());

        TweetDtoIn tweetDtoIn8 = TweetDtoIn.builder()
                .content("#musk 1469 Starlink satellites active 272 moving to operational orbits Laser links activate soon #hello")
                .build();

        Optional<Tweet> tweet8 = tweetService.addNewTweet(user1, tweetDtoIn8);
        System.out.println("Utworzono tweeta o id: " + tweet8.get().getId());

        System.out.println("test wyciagania hashtagow z tweeta nr.8: ");
        HashtagExtractor.extractHashtagStrings(tweet8.get()).forEach(System.out::println);
        hashtagService.findAll().forEach(System.out::println);
        System.out.println("Popular");
        hashtagService.getPopularHashtags().forEach(System.out::println);

        tweetService.addBookmark(user1,tweetDtoIn1);
        System.out.println("Dodano zakładke");

        tweetService.addComment(1,tweetDtoIn2);
        tweetService.addComment(1,tweetDtoIn2);


        System.out.println(tweetService.getTweetComments(1));

        System.out.println(tweetService.addTweetLike("jan", 1));
        System.out.println(tweetService.addTweetLike("jan", 2));
        System.out.println(tweetService.addTweetLike("jan", 3));

        User user3 = User.builder()
                .name("Radek")
                .surname("Pirat")
                .username("radek")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser3 = userRepository.save(user3);
        System.out.println("Utworzono usera o id: " + savedUser3.getId() + " oraz o nicku: " + user3.getUsername());

        User user4 = User.builder()
                .name("Karol")
                .surname("Karolek")
                .username("carlos123")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser4 = userRepository.save(user4);
        System.out.println("Utworzono usera o id: " + savedUser4.getId() + " oraz o nicku: " + user4.getUsername());

        User user5 = User.builder()
                .name("Adrian")
                .surname("Klos")
                .username("adik")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser5 = userRepository.save(user5);
        System.out.println("Utworzono usera o id: " + savedUser5.getId() + " oraz o nicku: " + user5.getUsername());

        User user6 = User.builder()
                .name("Maciej")
                .surname("Kruk")
                .username("kruk123")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser6 = userRepository.save(user6);
        System.out.println("Utworzono usera o id: " + savedUser6.getId() + " oraz o nicku: " + user6.getUsername());

        User user7 = User.builder()
                .name("Adam")
                .surname("Kosecki")
                .username("kosa123")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser7 = userRepository.save(user7);
        System.out.println("Utworzono usera o id: " + savedUser7.getId() + " oraz o nicku: " + user7.getUsername());

        User user8 = User.builder()
                .name("Dawid")
                .surname("Wolny")
                .username("wolny123")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser8 = userRepository.save(user8);
        System.out.println("Utworzono usera o id: " + savedUser8.getId() + " oraz o nicku: " + user8.getUsername());

        User user9 = User.builder()
                .name("Michal")
                .surname("Michalski")
                .username("michal123")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser9 = userRepository.save(user9);
        System.out.println("Utworzono usera o id: " + savedUser9.getId() + " oraz o nicku: " + user9.getUsername());

        User user10 = User.builder()
                .name("Tomek")
                .surname("Szybki")
                .username("szybki123")
                .password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy")
                .build();

        User savedUser10 = userRepository.save(user10);
        System.out.println("Utworzono usera o id: " + savedUser10.getId() + " oraz o nicku: " + user10.getUsername());

        TweetDtoIn tweetDtoIn9 = TweetDtoIn.builder()
                .content("Jem zupe")
                .author(savedUser3.getId())
                .build();

        Optional<Tweet> tweet9 = tweetService.addNewTweet(user3, tweetDtoIn9);
        System.out.println("Utworzono tweeta o id: " + tweet9.get().getId());

        TweetDtoIn tweetDtoIn10 = TweetDtoIn.builder()
                .content("jestem krolem")
                .build();

        Optional<Tweet> tweet10 = tweetService.addNewTweet(user4, tweetDtoIn10);
        System.out.println("Utworzono tweeta o id: " + tweet10.get().getId());

        TweetDtoIn tweetDtoIn11 = TweetDtoIn.builder()
                .content("nudzi mi sie")
                .build();

        Optional<Tweet> tweet11 = tweetService.addNewTweet(user5, tweetDtoIn11);
        System.out.println("Utworzono tweeta o id: " + tweet11.get().getId());

        TweetDtoIn tweetDtoIn12 = TweetDtoIn.builder()
                .content("ide na spacer")
                .build();

        Optional<Tweet> tweet12 = tweetService.addNewTweet(user6, tweetDtoIn12);
        System.out.println("Utworzono tweeta o id: " + tweet12.get().getId());

        TweetDtoIn tweetDtoIn13 = TweetDtoIn.builder()
                .content("lubie kebaby")
                .build();

        Optional<Tweet> tweet13 = tweetService.addNewTweet(user6, tweetDtoIn13);
        System.out.println("Utworzono tweeta o id: " + tweet13.get().getId());

        TweetDtoIn tweetDtoIn14 = TweetDtoIn.builder()
                .content(":D:D:D")
                .build();

        Optional<Tweet> tweet14 = tweetService.addNewTweet(user7, tweetDtoIn14);
        System.out.println("Utworzono tweeta o id: " + tweet14.get().getId());

        TweetDtoIn tweetDtoIn15 = TweetDtoIn.builder()
                .content("FAJNIE")
                .build();

        Optional<Tweet> tweet15 = tweetService.addNewTweet(user8, tweetDtoIn15);
        System.out.println("Utworzono tweeta o id: " + tweet15.get().getId());

        TweetDtoIn tweetDtoIn16 = TweetDtoIn.builder()
                .content("#ZIMA")
                .build();

        Optional<Tweet> tweet16 = tweetService.addNewTweet(user9, tweetDtoIn16);
        System.out.println("Utworzono tweeta o id: " + tweet16.get().getId());

        TweetDtoIn tweetDtoIn17 = TweetDtoIn.builder()
                .content("ODPOCZYWAM")
                .author(savedUser3.getId())
                .build();

        Optional<Tweet> tweet17 = tweetService.addNewTweet(user3, tweetDtoIn17);
        System.out.println("Utworzono tweeta o id: " + tweet17.get().getId());

        TweetDtoIn tweetDtoIn18 = TweetDtoIn.builder()
                .content("#hehehe")
                .build();

        Optional<Tweet> tweet18 = tweetService.addNewTweet(user4, tweetDtoIn18);
        System.out.println("Utworzono tweeta o id: " + tweet18.get().getId());

        TweetDtoIn tweetDtoIn19 = TweetDtoIn.builder()
                .content("#hehehe")
                .build();

        Optional<Tweet> tweet19 = tweetService.addNewTweet(user5, tweetDtoIn19);
        System.out.println("Utworzono tweeta o id: " + tweet19.get().getId());

        TweetDtoIn tweetDtoIn20 = TweetDtoIn.builder()
                .content("go go power rangers!")
                .build();

        Optional<Tweet> tweet20 = tweetService.addNewTweet(user6, tweetDtoIn20);
        System.out.println("Utworzono tweeta o id: " + tweet20.get().getId());

        TweetDtoIn tweetDtoIn21 = TweetDtoIn.builder()
                .content("lubie sport")
                .build();

        Optional<Tweet> tweet21 = tweetService.addNewTweet(user6, tweetDtoIn21);
        System.out.println("Utworzono tweeta o id: " + tweet21.get().getId());

        TweetDtoIn tweetDtoIn22 = TweetDtoIn.builder()
                .content("elo ")
                .build();

        Optional<Tweet> tweet22 = tweetService.addNewTweet(user7, tweetDtoIn22);
        System.out.println("Utworzono tweeta o id: " + tweet22.get().getId());

        TweetDtoIn tweetDtoIn23 = TweetDtoIn.builder()
                .content("nice to be me")
                .build();

        Optional<Tweet> tweet23 = tweetService.addNewTweet(user8, tweetDtoIn23);
        System.out.println("Utworzono tweeta o id: " + tweet23.get().getId());

        TweetDtoIn tweetDtoIn24 = TweetDtoIn.builder()
                .content("#LATO")
                .build();

        Optional<Tweet> tweet24 = tweetService.addNewTweet(user9, tweetDtoIn24);
        System.out.println("Utworzono tweeta o id: " + tweet24.get().getId());



    }
}
