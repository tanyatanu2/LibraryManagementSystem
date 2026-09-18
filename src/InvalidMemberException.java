/**
 * Thrown when a member ID is invalid, unregistered, or has exceeded
 * their borrowing limit.
 */
public class InvalidMemberException extends Exception {
    public InvalidMemberException(String message) {
        super(message);
    }
}
