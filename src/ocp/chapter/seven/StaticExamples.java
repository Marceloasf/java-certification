package ocp.chapter.seven;

public class StaticExamples {

    private static final int NUM_SECOND_PER_MINUTE;
    private static final int NUM_MINUTES_PER_HOUR;
    private static final int NUM_SECONDS_PER_HOUR;

    static {
        NUM_SECOND_PER_MINUTE = 60;
        NUM_MINUTES_PER_HOUR = 60;
    }

    static {
        NUM_SECONDS_PER_HOUR = NUM_SECOND_PER_MINUTE * NUM_MINUTES_PER_HOUR;
    }

    private String name = "Static class";
    public static void first() { }
    public static void second() { }
    public void third() { System.out.println(name); }

    public static void main(String... args) {

        first();
        second();
        // third(); // - Does not compile because main() is static and is trying to access a nonstatic method. 
                    // - If we add the static specifier to third(), it will only change the problem.
                    // - Because third is trying to access an Instance variable (nonstatic). Adding static
                    // to name would solve the problem. Another solution would have been to call third using an instance of the object

        new StaticExamples().third(); // Will compile normally

        System.out.println(NUM_SECONDS_PER_HOUR);
    }
}