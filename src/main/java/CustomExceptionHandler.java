import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.NoSuchFileException;

public class CustomExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof NoSuchFileException) {
            System.err.println("file wasn't found in directory");
        }
        else if (e instanceof IOException) {
            System.err.println("unable to read file properly");
        }
        else if (e instanceof Exception) {
            System.err.println("some error occured. Maybe file is empty");
        }
        System.err.println("finishing.....");
    }
}
