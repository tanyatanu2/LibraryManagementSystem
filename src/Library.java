import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central engine for the Library Management System.
 * Uses HashMap for O(1) book/member lookup (Collections Framework module)
 * and ArrayList to track active transactions.
 */
public class Library {
    private static final int LOAN_PERIOD_DAYS = 14;

    private final Map<String, Book> catalog = new HashMap<>();
    private final Map<String, Member> members = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private final Map<String, Integer> activeBorrowCount = new HashMap<>();

    private final TransactionLogger logger;
    private final LibraryDatabase db;

    public Library(TransactionLogger logger, LibraryDatabase db) {
        this.logger = logger;
        this.db = db;
    }

    public void addBook(Book book) {
        catalog.put(book.getIsbn(), book);
        db.saveBook(book);
        logger.log("Added book: " + book.getTitle());
    }

    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
        activeBorrowCount.put(member.getMemberId(), 0);
        db.saveMember(member);
        logger.log("Registered member: " + member.getName());
    }

    /** Thread-safe borrow operation - synchronized to prevent race conditions
     *  when multiple members borrow concurrently. */
    public synchronized Transaction borrowBook(String memberId, String isbn)
            throws BookNotAvailableException, InvalidMemberException {

        Member member = members.get(memberId);
        if (member == null) {
            throw new InvalidMemberException("No such member: " + memberId);
        }

        int currentCount = activeBorrowCount.getOrDefault(memberId, 0);
        if (currentCount >= member.getMaxBooksAllowed()) {
            throw new InvalidMemberException(member.getName() +
                    " has reached their borrowing limit of " + member.getMaxBooksAllowed());
        }

        Book book = catalog.get(isbn);
        if (book == null) {
            throw new BookNotAvailableException("No such book: " + isbn);
        }
        if (!book.tryBorrow()) {
            throw new BookNotAvailableException("'" + book.getTitle() + "' has no copies available");
        }

        LocalDate today = LocalDate.now();
        Transaction t = new Transaction(memberId, isbn, today, today.plusDays(LOAN_PERIOD_DAYS));
        transactions.add(t);
        activeBorrowCount.put(memberId, currentCount + 1);

        db.saveBook(book);
        db.saveTransaction(t);
        logger.log(member.getName() + " borrowed '" + book.getTitle() + "'");
        return t;
    }

    public synchronized double returnBook(String memberId, String isbn) throws InvalidMemberException {
        Transaction target = null;
        for (Transaction t : transactions) {
            if (t.getMemberId().equals(memberId) && t.getIsbn().equals(isbn) && !t.isReturned()) {
                target = t;
                break;
            }
        }
        if (target == null) {
            throw new InvalidMemberException("No active loan found for member " + memberId + " on " + isbn);
        }

        LocalDate today = LocalDate.now();
        target.markReturned(today);
        catalog.get(isbn).returnCopy();
        activeBorrowCount.merge(memberId, -1, Integer::sum);

        double fine = calculateFine(memberId, target, today);

        db.saveBook(catalog.get(isbn));
        db.saveTransaction(target);
        logger.log(members.get(memberId).getName() + " returned '" + catalog.get(isbn).getTitle() +
                "' (fine: Rs." + fine + ")");
        return fine;
    }

    private double calculateFine(String memberId, Transaction t, LocalDate returnDate) {
        long overdueDays = ChronoUnit.DAYS.between(t.getDueDate(), returnDate);
        if (overdueDays <= 0) return 0.0;
        Member m = members.get(memberId);
        return overdueDays * m.getFineRatePerDay();
    }

    public List<Book> searchByTitle(String keyword) {
        List<Book> results = new ArrayList<>();
        for (Book b : catalog.values()) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(b);
            }
        }
        return results;
    }

    public Collection<Book> allBooks() {
        return catalog.values();
    }

    public Collection<Member> allMembers() {
        return members.values();
    }
}
