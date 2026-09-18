import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * JDBC persistence layer. Uses SQLite (file-based, no server needed) so the
 * project runs out of the box. Swap the URL for MySQL/PostgreSQL in a real
 * deployment — the rest of the code is unaffected (JDBC abstraction).
 */
public class LibraryDatabase {
    private final String dbUrl;

    public LibraryDatabase(String dbFilePath) {
        this.dbUrl = "jdbc:sqlite:" + dbFilePath;
        initSchema();
    }

    private void initSchema() {
        String createBooks = "CREATE TABLE IF NOT EXISTS books (" +
                "isbn TEXT PRIMARY KEY, title TEXT, author TEXT, " +
                "total_copies INTEGER, available_copies INTEGER)";
        String createMembers = "CREATE TABLE IF NOT EXISTS members (" +
                "member_id TEXT PRIMARY KEY, name TEXT, type TEXT)";
        String createTransactions = "CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, member_id TEXT, isbn TEXT, " +
                "borrow_date TEXT, due_date TEXT, return_date TEXT)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createBooks);
            stmt.execute(createMembers);
            stmt.execute(createTransactions);
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    public void saveBook(Book book) {
        String sql = "INSERT OR REPLACE INTO books (isbn, title, author, total_copies, available_copies) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setInt(4, book.getTotalCopies());
            ps.setInt(5, book.getAvailableCopies());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save book: " + e.getMessage());
        }
    }

    public void saveMember(Member member) {
        String sql = "INSERT OR REPLACE INTO members (member_id, name, type) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getMemberId());
            ps.setString(2, member.getName());
            ps.setString(3, member.getClass().getSimpleName());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save member: " + e.getMessage());
        }
    }

    public void saveTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (member_id, isbn, borrow_date, due_date, return_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getMemberId());
            ps.setString(2, t.getIsbn());
            ps.setString(3, t.getBorrowDate().toString());
            ps.setString(4, t.getDueDate().toString());
            ps.setString(5, t.getReturnDate() == null ? null : t.getReturnDate().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save transaction: " + e.getMessage());
        }
    }
}
