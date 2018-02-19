package service;

import domain.ResultDuration;
import domain.TimePair;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

public class ResultsHolder {
    private static final String RESOURCE_DIR_PATH = FilenameUtils
        .separatorsToSystem("src/main/resources/");
    public static final Pattern fileNamePattern =
        Pattern.compile("(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)(.csv)*");
    private static ResultsHolder instance;

    private Map<String, ResultDuration> resultMap;

    private ResultsHolder() {
        resultMap = new HashMap<>();
    }

    public static ResultsHolder getResultsHolderInstance() throws IOException {
        if (instance == null) {
            instance = new ResultsHolder();
            instance.initHolder();
        }
        return instance;
    }

    public ResultDuration getResultDurationByDate(String date) {
        ResultDuration resultDuration = resultMap.get(date);
        if (resultDuration == null) throw new InvalidPathException(date + ".csv", "no such file");
        return resultDuration;
    }

    private void initHolder() throws IOException {
        List<Path> filePathes = Files
            .list(Paths.get(RESOURCE_DIR_PATH))
            .filter(p -> p.toString().endsWith(".csv"))
            .filter(p -> Files.isRegularFile(p))
            .filter(p -> fileNamePattern.matcher(p.getFileName().toString()).matches())
            .collect(Collectors.toList());

        for (Path filePath : filePathes) {
            List<TimePair> collect = new FileParser().parse(filePath);
            ResultDuration result = new TimeCounter().getTimeDuration(collect);
            resultMap.put(FilenameUtils.removeExtension(filePath.getFileName().toString()), result);
        }
    }
}
