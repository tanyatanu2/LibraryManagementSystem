import java.time.LocalDate;

/**
 * Represents a single borrow/return event. Used by TransactionLogger (file I/O)
 * and LibraryDatabase (JDBC persistence).
 */
public class Transaction {
    private String memberId;
    private String isbn;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate; // null until returned

    public Transaction(String memberId, String isbn, LocalDate borrowDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public String getMemberId() { return memberId; }
    public String getIsbn() { return isbn; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void markReturned(LocalDate date) { this.returnDate = date; }
    public boolean isReturned() { return returnDate != null; }

    @Override
    public String toString() {
        return String.format("Member:%s | ISBN:%s | Borrowed:%s | Due:%s | Returned:%s",
                memberId, isbn, borrowDate, dueDate,
                returnDate == null ? "NOT YET" : returnDate);
    }
}
