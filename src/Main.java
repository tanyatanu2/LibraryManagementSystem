/**
 * Entry point. Demonstrates:
 *  - OOP (Book, Member hierarchy)
 *  - Exception handling (custom checked exceptions)
 *  - File I/O (TransactionLogger)
 *  - Collections (HashMap/ArrayList inside Library)
 *  - Concurrency (multiple threads borrowing simultaneously)
 *  - JDBC (LibraryDatabase persists everything to SQLite)
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        TransactionLogger logger = new TransactionLogger("data/transactions.log");
        LibraryDatabase db = new LibraryDatabase("data/library.db");
        Library library = new Library(logger, db);

        // --- Seed catalog ---
        library.addBook(new Book("ISBN001", "Effective Java", "Joshua Bloch", 2));
        library.addBook(new Book("ISBN002", "Clean code ", "Robert C. Martin", 1));
        library.addBook(new Book("ISBN003", "Design Patterns", "Gang of Four", 3));

        // --- Register members (OOP: two subclasses of abstract Member) ---
        library.registerMember(new Student("S001", "Tanya"));
        library.registerMember(new Faculty("F001", "Dr. Rao"));

        System.out.println("=== Catalog ===");
        library.allBooks().forEach(System.out::println);

        // --- Normal borrow/return flow with exception handling ---
        try {
            library.borrowBook("S001", "ISBN001");
            double fine = library.returnBook("S001", "ISBN001");
            System.out.println("Fine on return: Rs." + fine);
        } catch (BookNotAvailableException | InvalidMemberException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // --- Trigger a custom exception deliberately: only 1 copy of Clean Code ---
        try {
            library.borrowBook("F001", "ISBN002");
            library.borrowBook("S001", "ISBN002"); // should fail, no copies left
        } catch (BookNotAvailableException | InvalidMemberException e) {
            System.out.println("Expected error caught: " + e.getMessage());
        }

        // --- Concurrency demo: multiple threads racing for the same book ---
        System.out.println("\n=== Concurrency Demo (racing for ISBN003, 3 copies, 5 members) ===");
        library.registerMember(new Student("S002", "Riya"));
        library.registerMember(new Student("S003", "Aman"));
        library.registerMember(new Faculty("F002", "Dr. Iyer"));

        String[] contenders = {"S001", "S002", "S003", "F001", "F002"};
        Thread[] threads = new Thread[contenders.length];
        for (int i = 0; i < contenders.length; i++) {
            threads[i] = new Thread(new BorrowTask(library, contenders[i], "ISBN003"),
                    "Thread-" + contenders[i]);
        }
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        System.out.println("\n=== Final Catalog State ===");
        library.allBooks().forEach(System.out::println);
        System.out.println("\nDone. See data/transactions.log for the full audit trail" +
                " and data/library.db for persisted records.");
    }
}
