package ocp.chapter.eight;

public class InitializationExample {

    public InitializationExample() {
        super();
        System.out.println(test1 + test2); // Will print Test X, since the static members are initialized before the instance is.
    }

    {
        test2 = "X";
    }

    private static String test1 = "Test ";

    private static String test2 = "this one too";

    static {
        test2 = "Y";
    }

    public static void main(String[] args) {

        new InitializationExample();
    }
}