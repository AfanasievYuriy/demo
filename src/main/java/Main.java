import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.util.Pair;

public class Main {

    public static void main(String[] args) throws IOException {
        //todo logging if string are wrong or other problems
        Pattern p = Pattern.compile(".*(вход\\b|выход\\b).+(([0-1][0-9]|2[0-3]):[0-5][0-9]).*");
       // Matcher m = p.matcher("   вход,   09:00-=-");
        Stream<String> stream = Files.lines(Paths.get("src/main/resources/file1.csv"));
        List<TimePair> collect = stream
            .map(s -> {
                Matcher matcher = p.matcher(s);
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

        Deque<TimePair> stack = new LinkedList<>();

        //todo if out on top ++ if in at the bottom
        stack.push(collect.get(0));
        for (int i = 1; i < collect.size(); i++) {
            if (collect.get(i).getAction() == ActionType.OUT
                && stack.peek().getAction() == ActionType.IN) {
                stack.push(collect.get(i));
            }
            else if (collect.get(i).getAction() == ActionType.IN
                && stack.peek().getAction() == ActionType.OUT
                && (i == collect.size() - 1 || collect.get(i + 1).getAction() != ActionType.IN)) {
                stack.push(collect.get(i));
            }
        }

        System.out.println(stack);


    }

}
