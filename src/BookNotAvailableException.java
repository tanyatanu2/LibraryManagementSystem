/**
 * Thrown when a member tries to borrow a book that has no copies left.
 */
public class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
