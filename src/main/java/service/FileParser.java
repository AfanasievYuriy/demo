package service;

import domain.ActionType;
import domain.TimePair;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileParser {
    private final static String pattern = ".*(вход\\b|выход\\b).+(([0-1][0-9]|2[0-3]):[0-5][0-9]).*";
    private final static Pattern fileLinePattern = Pattern.compile(pattern);

    public List<TimePair> parse(Path pathToFile) throws IOException {
        Stream<String> stream = Files.lines(pathToFile);
        List<TimePair> pairsFromFile = getValidTimePairs(stream);
        pairsFromFile = submitBoundaries(pairsFromFile);

        return pairsFromFile;
    }

    List<TimePair> submitBoundaries(List<TimePair> pairs) {
        if (pairs.get(pairs.size() - 1).getAction().equals(ActionType.IN)) {
            pairs.add(new TimePair(ActionType.OUT, LocalTime.MAX));
        }
        if (pairs.get(0).getAction().equals(ActionType.OUT)) {
            pairs.add(0, new TimePair(ActionType.IN, LocalTime.MIN));
        }
        return new ArrayList<>(pairs);
    }

    List<TimePair> getValidTimePairs(Stream<String> pairsStream) {
        return pairsStream
                .map(s -> {
                    Matcher matcher = fileLinePattern.matcher(s);
                    Optional<TimePair> pair = matcher.find()
                        ? Optional.of(
                        new TimePair(ActionType.parseActionType(matcher.group(1)),
                            LocalTime.parse(matcher.group(2))))
                        : Optional.empty();
                    return pair;
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
