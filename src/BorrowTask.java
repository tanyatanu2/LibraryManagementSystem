/**
 * Runnable that simulates one member attempting to borrow a book.
 * Multiple BorrowTasks run concurrently against the same Library instance
 * to demonstrate thread-safe access to shared state (Concurrency module).
 */
public class BorrowTask implements Runnable {
    private final Library library;
    private final String memberId;
    private final String isbn;

    public BorrowTask(Library library, String memberId, String isbn) {
        this.library = library;
        this.memberId = memberId;
        this.isbn = isbn;
    }

    @Override
    public void run() {
        try {
            Transaction t = library.borrowBook(memberId, isbn);
            System.out.println(Thread.currentThread().getName() +
                    " SUCCESS: " + memberId + " borrowed " + isbn + " (due " + t.getDueDate() + ")");
        } catch (BookNotAvailableException | InvalidMemberException e) {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED: " + e.getMessage());
        }
    }
}
