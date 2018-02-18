package service;

import domain.ActionType;
import domain.ResultDuration;
import domain.TimePair;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class TimeCounter {

    private Deque<TimePair> getValidPairs(List<TimePair> allPairsList) {
        Deque<TimePair> stack = new LinkedList<>();

        stack.push(allPairsList.get(0));
        for (int i = 1; i < allPairsList.size(); i++) {
            if (allPairsList.get(i).getAction() == ActionType.OUT
                && stack.peek().getAction() == ActionType.IN) {
                stack.push(allPairsList.get(i));
            }
            else if (allPairsList.get(i).getAction() == ActionType.IN
                && stack.peek().getAction() == ActionType.OUT
                && (i == allPairsList.size() - 1
                    || allPairsList.get(i + 1).getAction() != ActionType.IN)) {
                stack.push(allPairsList.get(i));
            }
        }
        return stack;
    }

    public ResultDuration getTimeDuration(List<TimePair> allPairsList) {
        Deque<TimePair> stack = getValidPairs(allPairsList);
        long resultMinutes = 0;
        boolean withErrors = false;

        if (stack.size() != allPairsList.size()) {
            withErrors = true;
        }

        while (!stack.isEmpty()) {
            LocalTime out = stack.pop().getTime();
            LocalTime in = stack.pop().getTime();
            resultMinutes += Duration.between(in, out).toMinutes();
        }

        return new ResultDuration(LocalTime.MIN.plusMinutes(resultMinutes), withErrors);
    }

}
