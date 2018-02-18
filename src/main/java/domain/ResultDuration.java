package domain;

import java.time.LocalTime;

public class ResultDuration {
    private LocalTime time;
    private boolean withError;

    public ResultDuration(LocalTime time, boolean withError) {
        this.time = time;
        this.withError = withError;
    }

    public LocalTime getTime() {
        return time;
    }

    public boolean isWithError() {
        return withError;
    }
}
