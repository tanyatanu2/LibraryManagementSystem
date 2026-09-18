public class Student extends Member {
    private static final int MAX_BOOKS = 3;
    private static final double FINE_RATE = 5.0;

    public Student(String memberId, String name) {
        super(memberId, name);
    }

    @Override
    public int getMaxBooksAllowed() { return MAX_BOOKS; }

    @Override
    public double getFineRatePerDay() { return FINE_RATE; }
}
