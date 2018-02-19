package service;

import static org.junit.Assert.*;

import domain.ActionType;
import domain.ResultDuration;
import domain.TimePair;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TimeCounterTest {
    private List<TimePair> pairsList1;
    private Deque<TimePair> expectedFromPairList1 = new LinkedList<>();
    private TimeCounter timeCounter = new TimeCounter();



    @Before
    public void setUp() throws Exception {
        pairsList1 = Arrays.asList(
            new TimePair(ActionType.IN, LocalTime.of(6, 0)),
            new TimePair(ActionType.OUT, LocalTime.of(14, 0)),
            new TimePair(ActionType.OUT, LocalTime.of(14, 30)),
            new TimePair(ActionType.IN, LocalTime.of(15, 0)),
            new TimePair(ActionType.IN, LocalTime.of(15, 30)),
            new TimePair(ActionType.OUT, LocalTime.of(17, 30)));
        expectedFromPairList1.push(new TimePair(ActionType.IN, LocalTime.of(6, 0)));
        expectedFromPairList1.push(new TimePair(ActionType.OUT, LocalTime.of(14, 0)));
        expectedFromPairList1.push(new TimePair(ActionType.IN, LocalTime.of(15, 30)));
        expectedFromPairList1.push(new TimePair(ActionType.OUT, LocalTime.of(17, 30)));

    }

    @Test
    public void validatePairsList_expectedSuccess() throws Exception {
        Deque<TimePair> actualOutput = timeCounter.validatePairsList(pairsList1);
        Assert.assertEquals(expectedFromPairList1, actualOutput);
    }

    @Test
    public void getTimeDuration() throws Exception {
        TimeCounter spy = Mockito.spy(TimeCounter.class);
        Mockito.when(spy.validatePairsList(pairsList1)).thenReturn(expectedFromPairList1);
        org.junit.Assert.assertEquals(
            spy.getTimeDuration(pairsList1),
            new ResultDuration(LocalTime.of(10, 0), true)
        );

    }

}