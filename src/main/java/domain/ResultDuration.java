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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResultDuration that = (ResultDuration) o;

        if (withError != that.withError) {
            return false;
        }
        return time.equals(that.time);
    }

    @Override
    public int hashCode() {
        int result = time.hashCode();
        result = 31 * result + (withError ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ResultDuration{" +
            "time=" + time +
            ", withError=" + withError +
            '}';
    }
}
