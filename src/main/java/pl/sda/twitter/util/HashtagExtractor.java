package pl.sda.twitter.util;

import pl.sda.twitter.model.Hashtag;
import pl.sda.twitter.model.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashtagExtractor {
    static public List<String> extractHashtagStrings(Tweet tweet) {
        ArrayList<String> matches = new ArrayList<String>();
        String content = tweet.getContent();
        Pattern pattern = Pattern.compile("#(\\S+)");
        Matcher mat = pattern.matcher(content);
        List<String> hashtags = new ArrayList<>();
        while (mat.find()) {
            //System.out.println(mat.group(1));
            hashtags.add(mat.group(1));
        }

        return hashtags;
    }
}
