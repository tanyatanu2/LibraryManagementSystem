/**
 * Abstract base class for all library members.
 * Demonstrates abstraction and inheritance — Student and Faculty
 * extend this with different borrowing limits and fine rates.
 */
public abstract class Member {
    private String memberId;
    private String name;

    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }

    /** Each member type defines its own borrowing cap. */
    public abstract int getMaxBooksAllowed();

    /** Each member type defines its own per-day overdue fine rate (in rupees). */
    public abstract double getFineRatePerDay();

    @Override
    public String toString() {
        return String.format("%s [%s] (%s) - max %d books, fine Rs.%.2f/day",
                name, memberId, getClass().getSimpleName(),
                getMaxBooksAllowed(), getFineRatePerDay());
    }
}
