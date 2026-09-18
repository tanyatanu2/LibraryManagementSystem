/**
 * Represents a book in the library catalog.
 * Demonstrates encapsulation: all fields are private with controlled access.
 */
public class Book {
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public Book(String isbn, String title, String author, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    /** Thread-safe decrement — synchronized so concurrent borrows can't
     *  push availableCopies below zero. */
    public synchronized boolean tryBorrow() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public synchronized void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    @Override
    public String toString() {
        return String.format("%-12s | %-30s | %-20s | %d/%d available",
                isbn, title, author, availableCopies, totalCopies);
    }
}
