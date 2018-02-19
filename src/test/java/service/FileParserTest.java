package service;

import domain.ActionType;
import domain.TimePair;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileParser.class, Files.class})

public class FileParserTest {
    private FileParser parser = new FileParser();
    private Stream<String> stream1;
    private Stream<String> stream2;

    private List<TimePair> expectedListFromStream1;
    private List<TimePair> expectedListFromStream2;

    @Before
    public void setUp() throws Exception {
        stream1 = Stream.of(
            "вход,12:00",
            "выход,14:00",
            "вхд,15:00"
        );
        stream2 = Stream.of(
            "выход,06:00",
            "вход,12:00",
            "выход,14:00",
            "вход,15:00"
        );
        expectedListFromStream1 = Arrays.asList(
            new TimePair(ActionType.IN, LocalTime.of(12, 0)),
            new TimePair(ActionType.OUT, LocalTime.of(14, 0)));
        expectedListFromStream2 = Arrays.asList(
            new TimePair(ActionType.IN, LocalTime.MIN),
            new TimePair(ActionType.OUT, LocalTime.of(6, 0)),
            new TimePair(ActionType.IN, LocalTime.of(12, 0)),
            new TimePair(ActionType.OUT, LocalTime.of(14, 0)),
            new TimePair(ActionType.IN, LocalTime.of(15, 0)),
            new TimePair(ActionType.OUT, LocalTime.MAX)
        );
        PowerMockito.mockStatic(Files.class);
        PowerMockito.when(Files.lines(Matchers.any(Path.class))).thenReturn(stream1);
    }

    @Test
    public void getTimePairs_expectedListFromStream1() throws Exception {
       List<TimePair> actualListFromStream1 = parser.getValidTimePairs(stream1);
        Assert.assertEquals(expectedListFromStream1, actualListFromStream1);
    }

    @Test
    public void submitBoundaries_expectedListFromStream2WithBounds() throws Exception {
        List<TimePair> actualListFromStream2 = parser.getValidTimePairs(stream2);
        actualListFromStream2 = parser.submitBoundaries(actualListFromStream2);
        Assert.assertEquals(actualListFromStream2, expectedListFromStream2);
    }

    @Test
    public void parse_expectedListFromStream2WithBounds() throws Exception {
        FileParser spy = Mockito.spy(parser);
        Mockito.when(spy.getValidTimePairs(stream1)).thenReturn(expectedListFromStream1);
        Mockito.when(spy.submitBoundaries(expectedListFromStream1))
            .thenReturn(expectedListFromStream1);
        spy.parse(Paths.get("any"));
        Mockito.verify(spy).getValidTimePairs(Mockito.eq(stream1));
        Mockito.verify(spy).submitBoundaries(Mockito.eq(expectedListFromStream1));
    }

}