import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Handles all file I/O for the system: appends a timestamped line to
 * transactions.log for every borrow/return/error event.
 * Demonstrates try-with-resources and checked-exception handling.
 */
public class TransactionLogger {
    private final String logFilePath;

    public TransactionLogger(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void log(String event) {
        try (FileWriter fw = new FileWriter(logFilePath, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println("[" + LocalDateTime.now() + "] " + event);
        } catch (IOException e) {
            System.err.println("Warning: could not write to log file - " + e.getMessage());
        }
    }
}
