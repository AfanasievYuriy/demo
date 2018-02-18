import domain.ResultDuration;
import domain.TimePair;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import service.FileParser;
import service.TimeCounter;

public class Main {
    public static final String TEST_FILE_PATH = "src/main/resources/file3.csv";

    public static void main(String[] args) {
        //todo logging if string are wrong or other problems
        List<TimePair> collect = null;
        try {
            collect = new FileParser()
                .parse(Paths.get(TEST_FILE_PATH));
        }
        catch (NoSuchFileException e) {
            System.err.println("file wasn't found in directory");
        }
        catch (IOException e) {
            System.err.println("unable to read file properly");
        }
        catch (Exception e) {
            System.err.println("some error occured");
        }

        if (collect == null) {
            System.err.println("finishing.....");
            return;
        }

        ResultDuration result = new TimeCounter().getTimeDuration(collect);
        LocalTime resultDuration = result.getTime();
        System.out.println(result.isWithError()
            ? "some data was corrupted. Approximate results are listed bellow"
            : "data is ok. Correct results are listed bellow");
        System.out.println("time in a building "
            + resultDuration.getHour() + "h"
            + ":" + resultDuration.getMinute() + "min");

    }

}
