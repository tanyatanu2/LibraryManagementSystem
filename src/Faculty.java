public class Faculty extends Member {
    private static final int MAX_BOOKS = 8;
    private static final double FINE_RATE = 2.0;

    public Faculty(String memberId, String name) {
        super(memberId, name);
    }

    @Override
    public int getMaxBooksAllowed() { return MAX_BOOKS; }

    @Override
    public double getFineRatePerDay() { return FINE_RATE; }
}
