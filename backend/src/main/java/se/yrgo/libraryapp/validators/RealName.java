package se.yrgo.libraryapp.validators;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RealName {
    private static Logger logger = LoggerFactory.getLogger(RealName.class);
    private static final Set<String> invalidWords = new HashSet<>();

    static {
        try (InputStream is = RealName.class.getClassLoader().getResourceAsStream("bad_words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            while (reader.readLine() != null) {
                invalidWords.addAll(reader.lines().collect(Collectors.toSet()));
            }
        } catch (IOException ex) {
            logger.error("Unable to initialize list of bad words", ex);
        }
    }

    private RealName() {}

    public static boolean validate(String name) {
        if (containsXSS(name)) {
            return false;
        }

        String cleanName = Utils.cleanAndUnLeet(name);
        String[] words = cleanName.split("\\W+");

        for (int i = 0; i < words.length; i++) {
            if (invalidWords.contains(words[i])) {
                return false;
            }
        }

        return true;
    }

    private static boolean containsXSS(String input) {
        String[] dangerousTags = {"<script", "<img", "<iframe", "<svg", "<object", "<embed"};
        for (String tag : dangerousTags) {
            if (input.toLowerCase().contains(tag)) {
                return true;
            }
        }

        String[] eventAttributes = {"onload", "onclick", "onerror", "onmouseover", "onfocus", "onkeydown"};
        for (String event : eventAttributes) {
            Pattern pattern = Pattern.compile("(?i)" + event + "=['\"][^'\"]*['\"]");
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                return true;
            }
        }

        String dangerousChars = "<>{}\"";
        for (int i = 0; i < dangerousChars.length(); i++) {
            if (input.indexOf(dangerousChars.charAt(i)) >= 0) {
                return true;
            }
        }

        return false;
    }
}
